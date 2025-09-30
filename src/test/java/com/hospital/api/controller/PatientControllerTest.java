package com.hospital.api.controller;

import com.hospital.api.dto.CreatePatientDto;
import com.hospital.api.dto.PatientResponseDto;
import com.hospital.api.dto.UpdatePatientDto;
import com.hospital.api.service.PatientService;
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

@DisplayName("PatientController - Unit Tests")
@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private PatientResponseDto testPatientDto;
    private CreatePatientDto createPatientDto;
    private UpdatePatientDto updatePatientDto;
    private List<PatientResponseDto> testPatientDtos;

    @BeforeEach
    void setUp() {
        testPatientDto = new PatientResponseDto(
                1L,
                "Jane",
                "Doe",
                "patient@test.com",
                "123-456-7890"
        );

        createPatientDto = new CreatePatientDto(
                "Jane",
                "Doe",
                "patient@test.com",
                "123-456-7890"
        );

        updatePatientDto = new UpdatePatientDto(
                "Jane",
                "Doe",
                "patient@test.com",
                "123-456-7890"
        );

        testPatientDtos = Arrays.asList(testPatientDto);
    }

    @Test
    @DisplayName("Should return list of patient DTOs")
    void getAllPatients_ShouldReturnListOfPatientDtos() {
        when(patientService.getAllPatients()).thenReturn(testPatientDtos);

        ResponseEntity<List<PatientResponseDto>> response = patientController.getAllPatients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPatientDtos, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(patientService).getAllPatients();
    }

    @Test
    @DisplayName("Should return patient DTO by ID")
    void getPatientById_ShouldReturnPatientDto() {
        when(patientService.getPatientById(1L)).thenReturn(testPatientDto);

        ResponseEntity<PatientResponseDto> response = patientController.getPatientById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPatientDto, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Jane", response.getBody().getFirstName());
        verify(patientService).getPatientById(1L);
    }

    @Test
    @DisplayName("Should create patient and return CREATED status with DTO")
    void createPatient_ShouldReturnCreatedPatientDto() {
        when(patientService.createPatient(any(CreatePatientDto.class))).thenReturn(testPatientDto);

        ResponseEntity<PatientResponseDto> response = patientController.createPatient(createPatientDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testPatientDto, response.getBody());
        assertNotNull(response.getBody());
        assertEquals("Jane", response.getBody().getFirstName());
        verify(patientService).createPatient(createPatientDto);
    }

    @Test
    @DisplayName("Should update patient and return updated patient DTO")
    void updatePatient_ShouldReturnUpdatedPatientDto() {
        when(patientService.updatePatient(eq(1L), any(UpdatePatientDto.class))).thenReturn(testPatientDto);

        ResponseEntity<PatientResponseDto> response = patientController.updatePatient(1L, updatePatientDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPatientDto, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(patientService).updatePatient(1L, updatePatientDto);
    }

    @Test
    @DisplayName("Should delete patient and return NO_CONTENT status")
    void deletePatient_ShouldReturnNoContent() {
        doNothing().when(patientService).deletePatient(1L);

        ResponseEntity<Void> response = patientController.deletePatient(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(patientService).deletePatient(1L);
    }


}