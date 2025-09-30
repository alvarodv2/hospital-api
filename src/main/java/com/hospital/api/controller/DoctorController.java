package com.hospital.api.controller;

import com.hospital.api.dto.CreateDoctorDto;
import com.hospital.api.dto.DoctorResponseDto;
import com.hospital.api.dto.UpdateDoctorDto;
import com.hospital.api.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "API for managing doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Get all doctors", description = "Retrieve a list of all doctors in the system")
    @GetMapping
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @Operation(summary = "Get doctor by ID", description = "Retrieve a specific doctor by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @Operation(summary = "Create a new doctor", description = "Add a new doctor to the system")
    @PostMapping
    public ResponseEntity<DoctorResponseDto> createDoctor(@Valid @RequestBody CreateDoctorDto createDoctorDto) {
        DoctorResponseDto createdDoctor = doctorService.createDoctor(createDoctorDto);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }

    @Operation(summary = "Update doctor", description = "Update the details of an existing doctor")
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> updateDoctor(@PathVariable Long id, @Valid @RequestBody UpdateDoctorDto updateDoctorDto) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, updateDoctorDto));
    }

    @Operation(summary = "Delete doctor", description = "Remove a doctor from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }


}
