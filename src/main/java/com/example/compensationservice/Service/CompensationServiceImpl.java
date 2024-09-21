package com.example.compensationservice.Service;

import com.example.compensationservice.Entities.Compensation;
import com.example.compensationservice.Exceptions.CompensationException;
import com.example.compensationservice.Repository.CompensationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompensationServiceImpl implements CompensationService{
    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public List<Compensation> getAllCompensations() {
        return compensationRepository.findAll();
    }

    @Override
    public Optional<Compensation> getCompensationById(Long id) {
        return Optional.ofNullable(compensationRepository.findById(id).orElseThrow(()-> new CompensationException("Compensation not found with id: " + id)));
    }

    @Override
    public void saveCompensation(Compensation compensation) {

    }

    @Override
    public void deleteCompensation(Long id) {
        compensationRepository.deleteById(id);
    }

    @Override
    public void updateCompensation(Compensation compensation) {

    }
}
