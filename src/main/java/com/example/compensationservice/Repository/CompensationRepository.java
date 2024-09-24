package com.example.compensationservice.Repository;

import com.example.compensationservice.Entities.Compensation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompensationRepository extends JpaRepository<Compensation, String> {
    List<Compensation> findByIdUser(String userId);
    boolean existsByDateCompensation(String date);
}
