package com.hospital.api.service;

import com.hospital.api.entity.Appointment;
import com.hospital.api.entity.Prescription;
import com.hospital.api.exception.notfound.PrescriptionNotFoundException;
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
import static org.mockito.Mockito.*;

class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @InjectMocks
    private PrescriptionService prescriptionService;

    private Prescription prescription1;
    private Prescription prescription2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        prescription1 = Prescription.builder()
                .id(1L)
                .medication("Medication A")
                .instructions("Take twice a day")
                .appointment(Appointment.builder().id(1L).build())
                .build();

        prescription2 = Prescription.builder()
                .id(2L)
                .medication("Medication B")
                .instructions("Take once a day")
                .appointment(Appointment.builder().id(2L).build())
                .build();
    }

    @Test
    void getAllPrescriptions_ShouldReturnListOfPrescriptions() {
        when(prescriptionRepository.findAll()).thenReturn(Arrays.asList(prescription1, prescription2));

        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();

        assertThat(prescriptions).hasSize(2).contains(prescription1, prescription2);
        verify(prescriptionRepository, times(1)).findAll();
    }

    @Test
    void getPrescriptionById_WhenExists_ShouldReturnPrescription() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription1));

        Prescription found = prescriptionService.getPrescriptionById(1L);

        assertThat(found).isEqualTo(prescription1);
        verify(prescriptionRepository, times(1)).findById(1L);
    }

    @Test
    void getPrescriptionById_WhenNotFound_ShouldThrowException() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PrescriptionNotFoundException.class, () -> prescriptionService.getPrescriptionById(1L));
        verify(prescriptionRepository, times(1)).findById(1L);
    }

    @Test
    void createPrescription_ShouldSaveAndReturnPrescription() {
        when(prescriptionRepository.save(prescription1)).thenReturn(prescription1);

        Prescription created = prescriptionService.createPrescription(prescription1);

        assertThat(created).isEqualTo(prescription1);
        verify(prescriptionRepository, times(1)).save(prescription1);
    }

    @Test
    void updatePrescription_WhenExists_ShouldUpdateAndReturnPrescription() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription1));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription1);

        Prescription updates = Prescription.builder()
                .medication("Updated Medication")
                .instructions("New instructions")
                .appointment(Appointment.builder().id(3L).build())
                .build();

        Prescription updated = prescriptionService.updatePrescription(1L, updates);

        assertThat(updated.getMedication()).isEqualTo("Updated Medication");
        assertThat(updated.getInstructions()).isEqualTo("New instructions");
        assertThat(updated.getAppointment().getId()).isEqualTo(3L);

        verify(prescriptionRepository, times(1)).findById(1L);
        verify(prescriptionRepository, times(1)).save(prescription1);
    }

    @Test
    void updatePrescription_WhenNotFound_ShouldThrowException() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.empty());

        Prescription updates = new Prescription();

        assertThrows(PrescriptionNotFoundException.class, () -> prescriptionService.updatePrescription(1L, updates));
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
