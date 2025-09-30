package com.hospital.api.controller;

import com.hospital.api.dto.CreateDoctorDto;
import com.hospital.api.dto.DoctorResponseDto;
import com.hospital.api.dto.UpdateDoctorDto;
import com.hospital.api.service.DoctorService;
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

@DisplayName("DoctorController - Unit Tests")
@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    private DoctorResponseDto testDoctorResponse;
    private List<DoctorResponseDto> testDoctorResponses;
    private CreateDoctorDto createDoctorDto;
    private UpdateDoctorDto updateDoctorDto;

    @BeforeEach
    void setUp() {
        testDoctorResponse = new DoctorResponseDto(
                1L,
                "John",
                "Test",
                "doctor@test.com",
                "Cardiology"
        );

        testDoctorResponses = Arrays.asList(testDoctorResponse);

        createDoctorDto = new CreateDoctorDto(
                "John",
                "Test",
                "doctor@test.com",
                "Cardiology"
        );

        updateDoctorDto = new UpdateDoctorDto(
                "John",
                "Test",
                "doctor@test.com",
                "Cardiology"
        );
    }

    @Test
    @DisplayName("Should return list of doctors")
    void getAllDoctors_ShouldReturnListOfDoctors() {
        when(doctorService.getAllDoctors()).thenReturn(testDoctorResponses);

        ResponseEntity<List<DoctorResponseDto>> response = doctorController.getAllDoctors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDoctorResponses, response.getBody());
        verify(doctorService).getAllDoctors();
    }

    @Test
    @DisplayName("Should return doctor by ID")
    void getDoctorById_ShouldReturnDoctor() {
        when(doctorService.getDoctorById(1L)).thenReturn(testDoctorResponse);

        ResponseEntity<DoctorResponseDto> response = doctorController.getDoctorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDoctorResponse, response.getBody());
        verify(doctorService).getDoctorById(1L);
    }

    @Test
    @DisplayName("Should create doctor and return CREATED status")
    void createDoctor_ShouldReturnCreatedDoctor() {
        when(doctorService.createDoctor(any(CreateDoctorDto.class))).thenReturn(testDoctorResponse);

        ResponseEntity<DoctorResponseDto> response = doctorController.createDoctor(createDoctorDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testDoctorResponse, response.getBody());
        verify(doctorService).createDoctor(createDoctorDto);
    }

    @Test
    @DisplayName("Should update doctor and return updated doctor")
    void updateDoctor_ShouldReturnUpdatedDoctor() {
        when(doctorService.updateDoctor(eq(1L), any(UpdateDoctorDto.class))).thenReturn(testDoctorResponse);

        ResponseEntity<DoctorResponseDto> response = doctorController.updateDoctor(1L, updateDoctorDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDoctorResponse, response.getBody());
        verify(doctorService).updateDoctor(1L, updateDoctorDto);
    }

    @Test
    @DisplayName("Should delete doctor and return NO_CONTENT status")
    void deleteDoctor_ShouldReturnNoContent() {
        doNothing().when(doctorService).deleteDoctor(1L);

        ResponseEntity<Void> response = doctorController.deleteDoctor(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorService).deleteDoctor(1L);
    }


}
