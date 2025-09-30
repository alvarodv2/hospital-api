package com.hospital.api.service;

import com.hospital.api.dto.CreatePrescriptionDto;
import com.hospital.api.dto.PrescriptionResponseDto;
import com.hospital.api.dto.UpdatePrescriptionDto;
import com.hospital.api.entity.Appointment;
import com.hospital.api.entity.Prescription;
import com.hospital.api.exception.notfound.PrescriptionNotFoundException;
import com.hospital.api.repository.AppointmentRepository;
import com.hospital.api.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    public List<PrescriptionResponseDto> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PrescriptionResponseDto getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(String.valueOf(id)));
        return convertToDto(prescription);
    }

    public PrescriptionResponseDto createPrescription(CreatePrescriptionDto createDto) {
        Appointment appointment = appointmentRepository.findById(createDto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + createDto.getAppointmentId()));

        Prescription prescription = Prescription.builder()
                .medication(createDto.getMedication())
                .instructions(createDto.getInstructions())
                .appointment(appointment)
                .build();

        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return convertToDto(savedPrescription);
    }

    public PrescriptionResponseDto updatePrescription(Long id, UpdatePrescriptionDto updateDto) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(String.valueOf(id)));

        updatePrescriptionFields(prescription, updateDto);

        Prescription updatedPrescription = prescriptionRepository.save(prescription);
        return convertToDto(updatedPrescription);
    }

    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(String.valueOf(id)));

        prescriptionRepository.delete(prescription);
    }

    private void updatePrescriptionFields(Prescription prescription, UpdatePrescriptionDto updateDto) {
        Optional.ofNullable(updateDto.getMedication()).ifPresent(prescription::setMedication);
        Optional.ofNullable(updateDto.getInstructions()).ifPresent(prescription::setInstructions);

        if (updateDto.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(updateDto.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + updateDto.getAppointmentId()));
            prescription.setAppointment(appointment);
        }
    }

    private PrescriptionResponseDto convertToDto(Prescription prescription) {
        return new PrescriptionResponseDto(
                prescription.getId(),
                prescription.getMedication(),
                prescription.getInstructions(),
                prescription.getAppointment() != null ? prescription.getAppointment().getId() : null
        );
    }


}