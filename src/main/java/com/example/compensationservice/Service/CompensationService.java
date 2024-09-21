package com.example.compensationservice.Service;

import com.example.compensationservice.Entities.Compensation;

import java.util.List;
import java.util.Optional;

public interface CompensationService {
    List<Compensation> getAllCompensations();
    Optional<Compensation> getCompensationById(Long id);
    void saveCompensation(Compensation compensation);
    void deleteCompensation(Long id);
    void updateCompensation(Compensation compensation);
}
