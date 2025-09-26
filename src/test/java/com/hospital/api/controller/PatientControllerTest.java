package com.hospital.api.controller;

import com.hospital.api.entity.Patient;
import com.hospital.api.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private Patient testPatient;
    private List<Patient> testPatients;

    @BeforeEach
    void setUp() {
        testPatient = Patient.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("patient@test.com")
                .numberPhone("123-456-7890")
                .build();

        testPatients = Arrays.asList(testPatient);
    }

    @Test
    void getAllPatients_ShouldReturnListOfPatients() {
        when(patientService.getAllPatients()).thenReturn(testPatients);

        ResponseEntity<List<Patient>> response = patientController.getAllPatients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPatients, response.getBody());
        verify(patientService).getAllPatients();
    }

    @Test
    void getPatientById_ShouldReturnPatient() {// Given
        when(patientService.getPatientById(1L)).thenReturn(testPatient);

        ResponseEntity<Patient> response = patientController.getPatientById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPatient, response.getBody());
        verify(patientService).getPatientById(1L);
    }

    @Test
    void createPatient_ShouldReturnCreatedPatient() {// Given
        when(patientService.createPatient(any(Patient.class))).thenReturn(testPatient);

        ResponseEntity<Patient> response = patientController.createPatient(testPatient);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testPatient, response.getBody());
        verify(patientService).createPatient(testPatient);
    }

    @Test
    void updatePatient_ShouldReturnUpdatedPatient() {
        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(testPatient);

        ResponseEntity<Patient> response = patientController.updatePatient(1L, testPatient);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPatient, response.getBody());
        verify(patientService).updatePatient(1L, testPatient);
    }

    @Test
    void deletePatient_ShouldReturnNoContent() {
        doNothing().when(patientService).deletePatient(1L);

        ResponseEntity<Void> response = patientController.deletePatient(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(patientService).deletePatient(1L);
    }


}