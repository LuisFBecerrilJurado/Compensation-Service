package com.example.compensationservice.Service;

import com.example.compensationservice.Entities.Compensation;
import com.example.compensationservice.Exceptions.CompensationException;
import com.example.compensationservice.Repository.CompensationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CompensationServiceImpl implements CompensationService{
    @Autowired
    private CompensationRepository compensationRepository;

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
        if(compensation.getType() == null) throw new CompensationException("Compensation type field is required");
        if(compensation.getAmount() == null) throw new CompensationException("Compensation amount field is required");
        if(compensation.getDateCompensation() == null) throw new CompensationException("Compensation date field is required");
        if(compensation.getIdUser() == null) throw new CompensationException("Id user field is required");
        if(!compensation.getType().equals("salary") && compensation.getDescription() == null) throw new CompensationException("Compensation description field is required");
    }

    private void validateAmountGreaterThanZero(Compensation compensation, String errorMessage) {
        if (compensation.getAmount() < 0) throw new CompensationException(errorMessage);
    }

    private void validateAmountDifferentFromZero(Compensation compensation, String errorMessage) {
        if (compensation.getAmount() == 0) throw new CompensationException(errorMessage);
    }

    private void compensationValidation(Compensation compensation) {
        if(compensationRepository.existsByDateCompensationAndIdUser(compensation.getDateCompensation(), compensation.getIdUser()))
            throw new CompensationException("A compensation already exists for this user on this date");
    }

    private void validateDate(Compensation compensation) {
        LocalDate currentDate = LocalDate.now();
        if(compensation.getLocalDate().isBefore(currentDate)) throw new CompensationException("Date must be after the current date");
    }

    @Override
    public void saveCompensation(Compensation compensation) {
        compensationValidation(compensation);
        validateFields(compensation);
        validateDate(compensation);
        generateAndSetUserId(compensation);
        final String amountLessThanZeroError = "Amount must be greater than 0";
        final String amountEqualZeroError = "Amount must be different than 0";
        switch (compensation.getType()) {
            case "salary" -> {
                compensation.setSalaryAdded(true);
                compensationRepository.save(compensation);
            }
            case "bonus", "commission", "allowance" -> {
                validateAmountGreaterThanZero(compensation, amountLessThanZeroError);
                compensationRepository.save(compensation);
            }
            case "adjustment" -> {
                validateAmountDifferentFromZero(compensation, amountEqualZeroError);
                compensationRepository.save(compensation);
            }
            default -> {
            }
        }
    }

    @Override
    public void deleteCompensation(String id) {
        compensationRepository.deleteById(id);
    }

    @Override
    public void updateCompensation(Compensation compensation) {

    }

    @Override
    public List<Compensation> getCompensationByIdUser(String idUser) {
        return compensationRepository.findByIdUser(idUser);
    }
}
