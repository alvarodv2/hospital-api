package com.hospital.api.controller;

import com.hospital.api.dto.CreatePatientDto;
import com.hospital.api.dto.PatientResponseDto;
import com.hospital.api.dto.UpdatePatientDto;
import com.hospital.api.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "API for managing patients")
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "Get all patients", description = "Retrieve a list of all patients in the system")
    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @Operation(summary = "Get patient by ID", description = "Retrieve a specific patient by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @Operation(summary = "Create a new patient", description = "Add a new patient to the system")
    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@RequestBody CreatePatientDto createPatientDto) {
        PatientResponseDto createdPatient = patientService.createPatient(createPatientDto);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @Operation(summary = "Update patient", description = "Update the details of an existing patient")
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable Long id, @RequestBody UpdatePatientDto updatePatientDto) {
        return ResponseEntity.ok(patientService.updatePatient(id, updatePatientDto));
    }

    @Operation(summary = "Delete patient", description = "Remove a patient from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }


}