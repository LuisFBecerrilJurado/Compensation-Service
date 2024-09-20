package com.example.compensationservice.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compensations")
@Builder
public class Compensation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)
    private LocalDate date;

    public Compensation(String description, double amount, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }
}
