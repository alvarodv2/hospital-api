package com.hospital.api.service;

import com.hospital.api.dto.CreatePrescriptionDto;
import com.hospital.api.dto.PrescriptionResponseDto;
import com.hospital.api.dto.UpdatePrescriptionDto;
import com.hospital.api.entity.Appointment;
import com.hospital.api.entity.Prescription;
import com.hospital.api.exception.notfound.PrescriptionNotFoundException;
import com.hospital.api.repository.AppointmentRepository;
import com.hospital.api.repository.PrescriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private PrescriptionService prescriptionService;

    private Prescription prescription1;
    private Prescription prescription2;
    private Appointment appointment1;
    private Appointment appointment2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        appointment1 = Appointment.builder().id(1L).build();
        appointment2 = Appointment.builder().id(2L).build();

        prescription1 = Prescription.builder()
                .id(1L)
                .medication("Medication A")
                .instructions("Take twice a day")
                .appointment(appointment1)
                .build();

        prescription2 = Prescription.builder()
                .id(2L)
                .medication("Medication B")
                .instructions("Take once a day")
                .appointment(appointment2)
                .build();
    }

    @Test
    void getAllPrescriptions_ShouldReturnListOfPrescriptionDtos() {
        when(prescriptionRepository.findAll()).thenReturn(Arrays.asList(prescription1, prescription2));

        List<PrescriptionResponseDto> prescriptions = prescriptionService.getAllPrescriptions();

        assertThat(prescriptions).hasSize(2);
        assertThat(prescriptions.get(0).getId()).isEqualTo(1L);
        assertThat(prescriptions.get(0).getMedication()).isEqualTo("Medication A");
        assertThat(prescriptions.get(0).getAppointmentId()).isEqualTo(1L);
        assertThat(prescriptions.get(1).getId()).isEqualTo(2L);
        verify(prescriptionRepository, times(1)).findAll();
    }

    @Test
    void getPrescriptionById_WhenExists_ShouldReturnPrescriptionDto() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription1));

        PrescriptionResponseDto found = prescriptionService.getPrescriptionById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getMedication()).isEqualTo("Medication A");
        assertThat(found.getInstructions()).isEqualTo("Take twice a day");
        assertThat(found.getAppointmentId()).isEqualTo(1L);
        verify(prescriptionRepository, times(1)).findById(1L);
    }

    @Test
    void getPrescriptionById_WhenNotFound_ShouldThrowException() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PrescriptionNotFoundException.class, () -> prescriptionService.getPrescriptionById(1L));
        verify(prescriptionRepository, times(1)).findById(1L);
    }

    @Test
    void createPrescription_ShouldSaveAndReturnPrescriptionDto() {
        CreatePrescriptionDto createDto = new CreatePrescriptionDto("Medication A", "Take twice a day", 1L);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment1));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription1);

        PrescriptionResponseDto created = prescriptionService.createPrescription(createDto);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getMedication()).isEqualTo("Medication A");
        assertThat(created.getInstructions()).isEqualTo("Take twice a day");
        assertThat(created.getAppointmentId()).isEqualTo(1L);
        verify(appointmentRepository, times(1)).findById(1L);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    void createPrescription_WhenAppointmentNotFound_ShouldThrowException() {
        CreatePrescriptionDto createDto = new CreatePrescriptionDto("Medication A", "Take twice a day", 999L);

        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> prescriptionService.createPrescription(createDto));
        verify(appointmentRepository, times(1)).findById(999L);
        verify(prescriptionRepository, never()).save(any(Prescription.class));
    }

    @Test
    void updatePrescription_WhenExists_ShouldUpdateAndReturnPrescriptionDto() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription1));
        when(appointmentRepository.findById(3L)).thenReturn(Optional.of(Appointment.builder().id(3L).build()));
        when(prescriptionRepository.save(any(Prescription.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UpdatePrescriptionDto updateDto = new UpdatePrescriptionDto("Updated Medication", "New instructions", 3L);

        PrescriptionResponseDto updated = prescriptionService.updatePrescription(1L, updateDto);

        assertThat(updated.getMedication()).isEqualTo("Updated Medication");
        assertThat(updated.getInstructions()).isEqualTo("New instructions");
        assertThat(updated.getAppointmentId()).isEqualTo(3L);

        verify(prescriptionRepository, times(1)).findById(1L);
        verify(appointmentRepository, times(1)).findById(3L);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    void updatePrescription_WithPartialData_ShouldOnlyUpdateProvidedFields() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription1));
        when(prescriptionRepository.save(any(Prescription.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UpdatePrescriptionDto updateDto = new UpdatePrescriptionDto();
        updateDto.setMedication("Updated Medication");

        PrescriptionResponseDto updated = prescriptionService.updatePrescription(1L, updateDto);

        assertThat(updated.getMedication()).isEqualTo("Updated Medication");
        assertThat(updated.getInstructions()).isEqualTo("Take twice a day");
        assertThat(updated.getAppointmentId()).isEqualTo(1L);

        verify(prescriptionRepository, times(1)).findById(1L);
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
        verify(appointmentRepository, never()).findById(any());
    }

    @Test
    void updatePrescription_WhenNotFound_ShouldThrowException() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());

        UpdatePrescriptionDto updateDto = new UpdatePrescriptionDto();

        assertThrows(PrescriptionNotFoundException.class, () -> prescriptionService.updatePrescription(1L, updateDto));
        verify(prescriptionRepository, times(1)).findById(1L);
    }

    @Test
    void deletePrescription_WhenExists_ShouldDeletePrescription() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription1));
        doNothing().when(prescriptionRepository).delete(prescription1);

        prescriptionService.deletePrescription(1L);

        verify(prescriptionRepository, times(1)).findById(1L);
        verify(prescriptionRepository, times(1)).delete(prescription1);
    }

    @Test
    void deletePrescription_WhenNotFound_ShouldThrowException() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PrescriptionNotFoundException.class, () -> prescriptionService.deletePrescription(1L));
        verify(prescriptionRepository, times(1)).findById(1L);
    }


}