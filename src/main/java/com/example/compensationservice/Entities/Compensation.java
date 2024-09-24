package com.example.compensationservice.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
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
    @Column(nullable = false)
    private String idUser;
    private String description;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private String dateCompensation;
    @Column(nullable = false)
    private String type;
    private Boolean salaryAdded;

    @Transient
    private LocalDate localDate;

    public Compensation(String description, double amount, LocalDate localDate, String type) {
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.localDate = localDate;
        this.dateCompensation = localDate.format(DateTimeFormatter.ofPattern("MM/yyyy"));
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        this.dateCompensation = localDate.format(DateTimeFormatter.ofPattern("MM/yyyy"));
    }
}
