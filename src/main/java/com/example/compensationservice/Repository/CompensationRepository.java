package com.example.compensationservice.Repository;

import com.example.compensationservice.Entities.Compensation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompensationRepository extends JpaRepository<Compensation, Long> {

}
