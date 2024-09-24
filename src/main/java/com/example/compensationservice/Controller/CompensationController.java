package com.example.compensationservice.Controller;

import com.example.compensationservice.Entities.Compensation;
import com.example.compensationservice.Exceptions.CompensationException;
import com.example.compensationservice.Service.CompensationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/compensations")
public class CompensationController {
    @Autowired
    private CompensationService compensationService;

    @GetMapping()
    public ResponseEntity<List<Compensation>> getAllCompensations() {
        return ResponseEntity.ok(compensationService.getAllCompensations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compensation> getCompensationById(@PathVariable String id) {
        return ResponseEntity.ok(compensationService.getCompensationById(id));
    }

    @PostMapping("/newCompensation")
    public ResponseEntity<String> saveCompensation(@RequestBody Compensation compensation) {
        compensationService.saveCompensation(compensation);
        return ResponseEntity.ok( "Compensation created successfully " );
    }

    public Compensation buildUpdateCompensation(Compensation existingCompensation, Compensation compensation) {
        return Compensation.builder()
                .idCompensation(existingCompensation.getIdCompensation())
                .amount(compensation.getAmount())
                .description(compensation.getDescription())
                .salaryAdded(compensation.getSalaryAdded())
                .type(compensation.getType())
                .dateCompensation(compensation.getDateCompensation())
                .build();
    }

    @PutMapping("/newCompensation/{id}")
    public ResponseEntity<String> updateCompensation(@PathVariable String id, @RequestBody Compensation compensation) {
        Optional<Compensation> compensationOptional = Optional.ofNullable(compensationService.getCompensationById(id));
        if(compensationOptional.isPresent()){
            Compensation existingCompensation = compensationOptional.get();
            Compensation updatedCompensation =  buildUpdateCompensation(existingCompensation, compensation);
            compensationService.updateCompensation(updatedCompensation);
            return ResponseEntity.status(HttpStatus.OK).body(" User Updated Successfully");
        }
        throw new CompensationException("Compensation not found with id: " + id);
    }

    @GetMapping("/users/{idUser}")
    public ResponseEntity<List<Compensation>> getCompensationByUserId(@PathVariable String idUser) {
        List<Compensation> compensationOptional = compensationService.getCompensationByIdUser(idUser);
        if(compensationOptional.isEmpty()) throw new CompensationException("Compensation not found with idUser: " + idUser);
        return ResponseEntity.ok(compensationService.getCompensationByIdUser(idUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompensation(@PathVariable String id) {
        compensationService.deleteCompensation(id);
        return ResponseEntity.ok("Compensation deleted successfully");
    }

}
