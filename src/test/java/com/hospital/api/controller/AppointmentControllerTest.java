package com.hospital.api.controller;

import com.hospital.api.entity.Appointment;
import com.hospital.api.entity.Doctor;
import com.hospital.api.entity.Patient;
import com.hospital.api.entity.Room;
import com.hospital.api.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("AppointmentController - Unit Tests")
@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    private Appointment testAppointment;
    private List<Appointment> testAppointments;

    @BeforeEach
    void setUp() {
        testAppointment = Appointment.builder()
                .id(1L)
                .appointmentDate(LocalDateTime.now())
                .notes("Test appointment")
                .patient(Patient.builder().id(1L).build())
                .doctor(Doctor.builder().id(1L).build())
                .room(Room.builder().id(1L).build())
                .build();

        testAppointments = Arrays.asList(testAppointment);
    }

    @Test
    @DisplayName("Should return list of appointments")
    void getAllAppointments_ShouldReturnListOfAppointments() {
        when(appointmentService.getAllAppointments()).thenReturn(testAppointments);

        ResponseEntity<List<Appointment>> response = appointmentController.getAllAppointments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testAppointments, response.getBody());
        verify(appointmentService).getAllAppointments();
    }

    @Test
    @DisplayName("Should return appointment by ID")
    void getAppointmentById_ShouldReturnAppointment() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(testAppointment);

        ResponseEntity<Appointment> response = appointmentController.getAppointmentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testAppointment, response.getBody());
        verify(appointmentService).getAppointmentById(1L);
    }

    @Test
    @DisplayName("Should create appointment and return CREATED status")
    void createAppointment_ShouldReturnCreatedAppointment() {
        when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(testAppointment);

        ResponseEntity<Appointment> response = appointmentController.createAppointment(testAppointment);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testAppointment, response.getBody());
        verify(appointmentService).createAppointment(testAppointment);
    }

    @Test
    @DisplayName("Should update appointment and return updated appointment")
    void updateAppointment_ShouldReturnUpdatedAppointment() {
        when(appointmentService.updateAppointment(eq(1L), any(Appointment.class))).thenReturn(testAppointment);

        ResponseEntity<Appointment> response = appointmentController.updateAppointment(1L, testAppointment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testAppointment, response.getBody());
        verify(appointmentService).updateAppointment(1L, testAppointment);
    }

    @Test
    @DisplayName("Should delete appointment and return NO_CONTENT status")
    void deleteAppointment_ShouldReturnNoContent() {
        doNothing().when(appointmentService).deleteAppointment(1L);

        ResponseEntity<Void> response = appointmentController.deleteAppointment(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(appointmentService).deleteAppointment(1L);
    }


}
