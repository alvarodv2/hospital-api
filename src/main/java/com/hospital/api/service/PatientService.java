package com.hospital.api.service;

import com.hospital.api.dto.CreatePatientDto;
import com.hospital.api.dto.PatientResponseDto;
import com.hospital.api.dto.UpdatePatientDto;
import com.hospital.api.entity.Patient;
import com.hospital.api.exception.notfound.ResourceNotFoundException.Builder;
import com.hospital.api.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<PatientResponseDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PatientResponseDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> Builder.patient(String.valueOf(id)));
        return convertToDto(patient);
    }

    public PatientResponseDto createPatient(CreatePatientDto createDto) {
        Patient patient = Patient.builder()
                .firstName(createDto.getFirstName())
                .lastName(createDto.getLastName())
                .email(createDto.getEmail())
                .numberPhone(createDto.getNumberPhone())
                .build();

        Patient savedPatient = patientRepository.save(patient);
        return convertToDto(savedPatient);
    }

    public PatientResponseDto updatePatient(Long id, UpdatePatientDto updateDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> Builder.patient(String.valueOf(id)));

        updatePatientFields(patient, updateDto);

        Patient updatedPatient = patientRepository.save(patient);
        return convertToDto(updatedPatient);
    }

    private void updatePatientFields(Patient patient, UpdatePatientDto updateDto) {
        if (updateDto.getFirstName() != null) {
            patient.setFirstName(updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null) {
            patient.setLastName(updateDto.getLastName());
        }
        if (updateDto.getEmail() != null) {
            patient.setEmail(updateDto.getEmail());
        }
        if (updateDto.getNumberPhone() != null) {
            patient.setNumberPhone(updateDto.getNumberPhone());
        }
    }

    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> Builder.patient(String.valueOf(id)));

        patientRepository.delete(patient);
    }

    private PatientResponseDto convertToDto(Patient patient) {
        return new PatientResponseDto(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getNumberPhone()
        );
    }
}