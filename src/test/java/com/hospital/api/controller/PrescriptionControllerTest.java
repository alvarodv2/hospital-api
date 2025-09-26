package com.hospital.api.controller;

import com.hospital.api.entity.Appointment;
import com.hospital.api.entity.Prescription;
import com.hospital.api.service.PrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("PrescriptionController - Unit Tests")
@ExtendWith(MockitoExtension.class)
class PrescriptionControllerTest {

    @Mock
    private PrescriptionService prescriptionService;

    @InjectMocks
    private PrescriptionController prescriptionController;

    private Prescription testPrescription;
    private List<Prescription> testPrescriptions;

    @BeforeEach
    void setUp() {
        testPrescription = Prescription.builder()
                .id(1L)
                .medication("Test Medication")
                .instructions("Take 1 pill daily, once a day")
                .appointment(Appointment.builder().id(1L).build())
                .build();

        testPrescriptions = Arrays.asList(testPrescription);
    }

    @Test
    @DisplayName("Should return list of prescriptions")
    void getAllPrescriptions_ShouldReturnListOfPrescriptions() {
        when(prescriptionService.getAllPrescriptions()).thenReturn(testPrescriptions);

        ResponseEntity<List<Prescription>> response = prescriptionController.getAllPrescriptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPrescriptions, response.getBody());
        verify(prescriptionService).getAllPrescriptions();
    }

    @Test
    @DisplayName("Should return prescription by ID")
    void getPrescriptionById_ShouldReturnPrescription() {
        when(prescriptionService.getPrescriptionById(1L)).thenReturn(testPrescription);

        ResponseEntity<Prescription> response = prescriptionController.getPrescriptionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPrescription, response.getBody());
        verify(prescriptionService).getPrescriptionById(1L);
    }

    @Test
    @DisplayName("Should create prescription and return CREATED status")
    void createPrescription_ShouldReturnCreatedPrescription() {
        when(prescriptionService.createPrescription(any(Prescription.class))).thenReturn(testPrescription);

        ResponseEntity<Prescription> response = prescriptionController.createPrescription(testPrescription);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testPrescription, response.getBody());
        verify(prescriptionService).createPrescription(testPrescription);
    }

    @Test
    @DisplayName("Should update prescription and return updated prescription")
    void updatePrescription_ShouldReturnUpdatedPrescription() {
        when(prescriptionService.updatePrescription(eq(1L), any(Prescription.class))).thenReturn(testPrescription);

        ResponseEntity<Prescription> response = prescriptionController.updatePrescription(1L, testPrescription);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPrescription, response.getBody());
        verify(prescriptionService).updatePrescription(1L, testPrescription);
    }

    @Test
    @DisplayName("Should delete prescription and return NO_CONTENT status")
    void deletePrescription_ShouldReturnNoContent() {
        doNothing().when(prescriptionService).deletePrescription(1L);

        ResponseEntity<Void> response = prescriptionController.deletePrescription(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(prescriptionService).deletePrescription(1L);
    }


}
