package com.example.compensationservice.Service;

import com.example.compensationservice.Entities.Compensation;

import java.util.List;

public interface CompensationService {
    List<Compensation> getAllCompensations();
    Compensation getCompensationById(String id);
    List<Compensation> getCompensationByIdUser(String idUser);
    void saveCompensation(Compensation compensation);
    void deleteCompensation(String id);
    void updateCompensation(Compensation compensation);
}
