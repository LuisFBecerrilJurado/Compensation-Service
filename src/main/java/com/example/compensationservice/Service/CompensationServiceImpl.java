package com.example.compensationservice.Service;

import com.example.compensationservice.Entities.Compensation;
import com.example.compensationservice.Exceptions.CompensationException;
import com.example.compensationservice.Repository.CompensationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CompensationServiceImpl implements CompensationService{
    private final CompensationRepository compensationRepository;

    public CompensationServiceImpl(CompensationRepository compensationRepository) {
        this.compensationRepository = compensationRepository;
    }

    @Override
    public List<Compensation> getAllCompensations() {
        return compensationRepository.findAll();
    }

    @Override
    public Compensation getCompensationById(String id) {
        return compensationRepository.findById(id).orElseThrow(() -> new CompensationException("Compensation not found with id: " + id));
    }

    private void generateAndSetUserId(Compensation compensation) {
        String randomCompensationId = UUID.randomUUID().toString();
        compensation.setIdCompensation(randomCompensationId);
    }

    public void validateFields(Compensation compensation) {
        if(compensation.getType() == null || compensation.getType().isEmpty() ) throw new CompensationException("Compensation Type field is required");
        if(compensation.getAmount() == null) throw new CompensationException("Compensation Amount field is required");
        if(compensation.getIdUser() == null || compensation.getIdUser().isEmpty() ) throw new CompensationException("Compensation IdUser field is required");
        if(!compensation.getType().equals("salary") && (compensation.getDescription() == null || compensation.getDescription().isEmpty()))
            throw new CompensationException("Compensation description field is required");
    }

    private void compensationValidation(Compensation compensation) {
        if(compensationRepository.existsByDateCompensationAndIdUser(compensation.getDateCompensation(), compensation.getIdUser()))
            throw new CompensationException("A compensation already exists for this user on this date");
    }

    private void validateDate(Compensation compensation) {
        if(compensation.getLocalDate() == null) throw new CompensationException("Compensation Date field is required");
        LocalDate currentDate = LocalDate.now();
        if(compensation.getLocalDate().isBefore(currentDate)) throw new CompensationException("Date must be after the current date");
    }

    @Override
    public void saveCompensation(Compensation compensation) {
        compensationValidation(compensation);
        validateFields(compensation);
        validateDate(compensation);
        generateAndSetUserId(compensation);
        switch (compensation.getType()) {
            case "salary" -> {
                compensation.setSalaryAdded(true);
                compensationRepository.save(compensation);
            }
            case "bonus", "commission", "allowance" -> {
                if (compensation.getAmount() < 0) throw new CompensationException("Amount must be greater than 0");
                compensation.setSalaryAdded(false);
                compensationRepository.save(compensation);
            }
            case "adjustment" -> {
                if (compensation.getAmount() == 0) throw new CompensationException("Amount must be different than 0");
                compensation.setSalaryAdded(false);
                compensationRepository.save(compensation);
            }
            default -> {
            }
        }
    }

    private void validateSalary(Compensation compensation) {
        if(compensation.getSalaryAdded()){
            if(compensation.getType().equals("salary")) throw new CompensationException("Only one salary can be added per compensation");
        }
    }

    @Override
    public void updateCompensation(Compensation compensation) {
        validateSalary(compensation);
        validateFields(compensation);
        if (compensation.getLocalDate() != null) validateDate(compensation);
        if (compensation.getSalaryAdded()) {
            if (compensation.getType().equals("salary"))
                throw new CompensationException("Only can add one salary per Compensation");
        }
        compensationRepository.save(compensation);
    }

    @Override
    public void deleteCompensation(String id) {
        compensationRepository.deleteById(id);
    }


    @Override
    public List<Compensation> getCompensationByIdUser(String idUser) {
        return compensationRepository.findByIdUser(idUser);
    }
}
