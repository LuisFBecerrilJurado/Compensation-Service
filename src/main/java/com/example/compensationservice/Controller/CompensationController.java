package com.example.compensationservice.Controller;

import com.example.compensationservice.Entities.Compensation;
import com.example.compensationservice.Exceptions.CompensationException;
import com.example.compensationservice.Service.CompensationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/compensations")
public class CompensationController {
    private final CompensationService compensationService;

    public CompensationController(CompensationService compensationService) {
        this.compensationService = compensationService;
    }

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
        return ResponseEntity.ok( "Compensation created successfully" );
    }

    public Compensation createUpdateCompensation(Compensation existingCompensation, Compensation compensation) {
        if(!compensation.getType().equals("salary") && (compensation.getDescription() == null || compensation.getDescription().isEmpty()))
            throw new CompensationException("Compensation description field is required");
        return Compensation.builder()
                .idCompensation(existingCompensation.getIdCompensation())
                .amount(compensation.getAmount() + existingCompensation.getAmount())
                .description(compensation.getDescription() + " " + existingCompensation.getDescription())
                .salaryAdded(existingCompensation.getSalaryAdded())
                .type(compensation.getType())
                .localDate(compensation.getLocalDate())
                .dateCompensation(compensation.getDateCompensation())
                .idUser(existingCompensation.getIdUser())
                .build();
    }

    @PutMapping("/newCompensation/{id}")
    public ResponseEntity<String> updateCompensation(@PathVariable String id, @RequestBody Compensation compensation) {
        Optional<Compensation> compensationOptional = Optional.ofNullable(compensationService.getCompensationById(id));
        if(compensationOptional.isPresent()){
            Compensation existingCompensation = compensationOptional.get();
            Compensation updatedCompensation = createUpdateCompensation(existingCompensation, compensation);
            compensationService.updateCompensation(updatedCompensation);
            return ResponseEntity.status(HttpStatus.OK).body("Compensation Updated Successfully");
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
        Optional<Compensation> compensationOptional = Optional.ofNullable(compensationService.getCompensationById(id));
        if(compensationOptional.isEmpty()) throw new CompensationException("Compensation not found compensation with id: " + id);
        compensationService.deleteCompensation(id);
        return ResponseEntity.ok("Compensation deleted successfully");
    }

}
