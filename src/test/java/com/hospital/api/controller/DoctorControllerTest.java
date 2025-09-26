package com.hospital.api.controller;

import com.hospital.api.entity.Doctor;
import com.hospital.api.service.DoctorService;
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
class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    private Doctor testDoctor;
    private List<Doctor> testDoctors;

    @BeforeEach
    void setUp() {
        testDoctor = Doctor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Test")
                .specialty("Cardiology")
                .email("doctor@test.com")
                .build();

        testDoctors = Arrays.asList(testDoctor);
    }

    @Test
    void getAllDoctors_ShouldReturnListOfDoctors() {
        when(doctorService.getAllDoctors()).thenReturn(testDoctors);

        ResponseEntity<List<Doctor>> response = doctorController.getAllDoctors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDoctors, response.getBody());
        verify(doctorService).getAllDoctors();
    }

    @Test
    void getDoctorById_ShouldReturnDoctor() {
        when(doctorService.getDoctorById(1L)).thenReturn(testDoctor);

        ResponseEntity<Doctor> response = doctorController.getDoctorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDoctor, response.getBody());
        verify(doctorService).getDoctorById(1L);
    }

    @Test
    void createDoctor_ShouldReturnCreatedDoctor() {
        when(doctorService.createDoctor(any(Doctor.class))).thenReturn(testDoctor);

        ResponseEntity<Doctor> response = doctorController.createDoctor(testDoctor);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testDoctor, response.getBody());
        verify(doctorService).createDoctor(testDoctor);
    }

    @Test
    void updateDoctor_ShouldReturnUpdatedDoctor() {
        when(doctorService.updateDoctor(eq(1L), any(Doctor.class))).thenReturn(testDoctor);

        ResponseEntity<Doctor> response = doctorController.updateDoctor(1L, testDoctor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testDoctor, response.getBody());
        verify(doctorService).updateDoctor(1L, testDoctor);
    }

    @Test
    void deleteDoctor_ShouldReturnNoContent() {// Given
        doNothing().when(doctorService).deleteDoctor(1L);

        ResponseEntity<Void> response = doctorController.deleteDoctor(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorService).deleteDoctor(1L);
    }


}