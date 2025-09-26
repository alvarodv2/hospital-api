package com.hospital.api.controller;

import com.hospital.api.entity.Prescription;
import com.hospital.api.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@Tag(name = "Prescriptions", description = "API for managing prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Operation(summary = "Get all prescriptions", description = "Retrieve a list of all prescriptions")
    @GetMapping
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions());
    }

    @Operation(summary = "Get prescription by ID", description = "Retrieve a prescription by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @Operation(summary = "Create a prescription", description = "Add a new prescription to the system")
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        Prescription createdPrescription = prescriptionService.createPrescription(prescription);
        return new ResponseEntity<>(createdPrescription, HttpStatus.CREATED);
    }

    @Operation(summary = "Update prescription", description = "Update an existing prescription by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id, @RequestBody Prescription prescriptionDetails) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(id, prescriptionDetails));
    }

    @Operation(summary = "Delete prescription", description = "Delete a prescription by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }


}
