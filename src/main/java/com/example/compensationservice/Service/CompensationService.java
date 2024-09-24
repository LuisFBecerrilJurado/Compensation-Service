package com.example.compensationservice.Service;

import com.example.compensationservice.Entities.Compensation;

import java.time.LocalDate;
import java.util.List;

public interface CompensationService {
    List<Compensation> getAllCompensations();
    Compensation getCompensationById(String id);
    List<Compensation> getCompensationByUserId(String userId);
    Compensation saveCompensation(Compensation compensation);
    void deleteCompensation(String id);
    void updateCompensation(Compensation compensation);
}
