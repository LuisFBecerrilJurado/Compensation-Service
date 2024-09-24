package com.example.compensationservice.Service;

import com.example.compensationservice.Entities.Compensation;
import com.example.compensationservice.Exceptions.CompensationException;
import com.example.compensationservice.Repository.CompensationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompensationServiceImpl implements CompensationService{
    @Autowired
    private CompensationRepository compensationRepository;
    private double amount;

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
        if(compensationRepository.existsByDateCompensation(compensation.getDateCompensation()))
            throw new CompensationException("Compensation already exits with this date");
    }

    @Override
    public Compensation saveCompensation(Compensation compensation) {
        compensationValidation(compensation);
        validateFields(compensation);
        generateAndSetUserId(compensation);
        final String amountLessThanZeroError = "Amount must be greater than 0";
        final String amountEqualZeroError = "Amount must be different than 0";
        return switch (compensation.getType()) {
            case "salary" -> {
                compensation.setSalaryAdded(true);
                yield compensationRepository.save(compensation);
            }
            case "bonus", "commission", "allowance" -> {
                validateAmountGreaterThanZero(compensation, amountLessThanZeroError);
                yield compensationRepository.save(compensation);
            }
            case "adjustment" -> {
                validateAmountDifferentFromZero(compensation, amountEqualZeroError);
                yield compensationRepository.save(compensation);
            }
            default -> compensation;
        };
    }

    @Override
    public void deleteCompensation(String id) {
        compensationRepository.deleteById(id);
    }

    @Override
    public void updateCompensation(Compensation compensation) {

    }

    @Override
    public List<Compensation> getCompensationByUserId(String userId) {
        return compensationRepository.findByIdUser(userId);
    }
}
