package com.hospital.api.controller;

import com.hospital.api.dto.CreatePrescriptionDto;
import com.hospital.api.dto.PrescriptionResponseDto;
import com.hospital.api.dto.UpdatePrescriptionDto;
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

    private PrescriptionResponseDto testPrescriptionDto;
    private CreatePrescriptionDto createPrescriptionDto;
    private UpdatePrescriptionDto updatePrescriptionDto;
    private List<PrescriptionResponseDto> testPrescriptionDtos;

    @BeforeEach
    void setUp() {
        testPrescriptionDto = new PrescriptionResponseDto(
                1L,
                "Test Medication",
                "Take 1 pill daily, once a day",
                1L
        );

        createPrescriptionDto = new CreatePrescriptionDto(
                "Test Medication",
                "Take 1 pill daily, once a day",
                1L
        );

        updatePrescriptionDto = new UpdatePrescriptionDto(
                "Updated Medication",
                "Take 2 pills daily",
                1L
        );

        testPrescriptionDtos = Arrays.asList(testPrescriptionDto);
    }

    @Test
    @DisplayName("Should return list of prescription DTOs")
    void getAllPrescriptions_ShouldReturnListOfPrescriptionDtos() {
        when(prescriptionService.getAllPrescriptions()).thenReturn(testPrescriptionDtos);

        ResponseEntity<List<PrescriptionResponseDto>> response = prescriptionController.getAllPrescriptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPrescriptionDtos, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(prescriptionService).getAllPrescriptions();
    }

    @Test
    @DisplayName("Should return prescription DTO by ID")
    void getPrescriptionById_ShouldReturnPrescriptionDto() {
        when(prescriptionService.getPrescriptionById(1L)).thenReturn(testPrescriptionDto);

        ResponseEntity<PrescriptionResponseDto> response = prescriptionController.getPrescriptionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPrescriptionDto, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Test Medication", response.getBody().getMedication());
        verify(prescriptionService).getPrescriptionById(1L);
    }

    @Test
    @DisplayName("Should create prescription and return CREATED status with DTO")
    void createPrescription_ShouldReturnCreatedPrescriptionDto() {
        when(prescriptionService.createPrescription(any(CreatePrescriptionDto.class))).thenReturn(testPrescriptionDto);

        ResponseEntity<PrescriptionResponseDto> response = prescriptionController.createPrescription(createPrescriptionDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testPrescriptionDto, response.getBody());
        assertNotNull(response.getBody());
        assertEquals("Test Medication", response.getBody().getMedication());
        verify(prescriptionService).createPrescription(createPrescriptionDto);
    }

    @Test
    @DisplayName("Should update prescription and return updated prescription DTO")
    void updatePrescription_ShouldReturnUpdatedPrescriptionDto() {
        when(prescriptionService.updatePrescription(eq(1L), any(UpdatePrescriptionDto.class))).thenReturn(testPrescriptionDto);

        ResponseEntity<PrescriptionResponseDto> response = prescriptionController.updatePrescription(1L, updatePrescriptionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPrescriptionDto, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(prescriptionService).updatePrescription(1L, updatePrescriptionDto);
    }

    @Test
    @DisplayName("Should delete prescription and return NO_CONTENT status")
    void deletePrescription_ShouldReturnNoContent() {
        doNothing().when(prescriptionService).deletePrescription(1L);

        ResponseEntity<Void> response = prescriptionController.deletePrescription(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(prescriptionService).deletePrescription(1L);
    }



}