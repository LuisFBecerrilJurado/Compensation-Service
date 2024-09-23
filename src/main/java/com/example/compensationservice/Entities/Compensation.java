package com.example.compensationservice.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compensations")
@Builder
public class Compensation {
    @Id
    private String  idCompensation;
    private String description;
    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)
    private LocalDate dateCompensation;
    private String type;
    private Boolean salaryAdded;

    @Transient
    private LocalDate localDate;

    public Compensation(String description, double amount, LocalDate date, String type) {
        this.description = description;
        this.amount = amount;
        this.dateCompensation = date;
        this.type = type;
    }

    public void setDateCompensation(LocalDate localDate) {
        this.localDate = localDate;
        this.dateCompensation = LocalDate.parse(localDate.format(DateTimeFormatter.ofPattern("MM/yyyy")));
    }
}
