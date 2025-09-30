package com.hospital.api.controller;

import com.hospital.api.dto.AppointmentResponseDto;
import com.hospital.api.dto.CreateAppointmentDto;
import com.hospital.api.dto.UpdateAppointmentDto;
import com.hospital.api.dto.summary.DoctorSummaryDto;
import com.hospital.api.dto.summary.PatientSummaryDto;
import com.hospital.api.dto.summary.RoomSummaryDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private AppointmentResponseDto testAppointmentDto;
    private List<AppointmentResponseDto> testAppointmentsDto;
    private CreateAppointmentDto createAppointmentDto;
    private UpdateAppointmentDto updateAppointmentDto;

    @BeforeEach
    void setUp() {
        testAppointmentDto = new AppointmentResponseDto(
                1L,
                LocalDateTime.now(),
                "Test appointment",
                new PatientSummaryDto(1L, "John", "Doe"),
                new DoctorSummaryDto(1L, "Dr. Smith", "Cardiology"),
                new RoomSummaryDto(1L, "101")
        );

        createAppointmentDto = new CreateAppointmentDto();
        createAppointmentDto.setAppointmentDate(LocalDateTime.now());
        createAppointmentDto.setNotes("New appointment");
        createAppointmentDto.setDoctorId(1L);
        createAppointmentDto.setPatientId(1L);
        createAppointmentDto.setRoomId(1L);

        updateAppointmentDto = new UpdateAppointmentDto();
        updateAppointmentDto.setAppointmentDate(LocalDateTime.now().plusDays(1));
        updateAppointmentDto.setNotes("Updated appointment");
        updateAppointmentDto.setDoctorId(2L);
        updateAppointmentDto.setPatientId(2L);
        updateAppointmentDto.setRoomId(2L);

        testAppointmentsDto = Arrays.asList(testAppointmentDto);
    }

    @Test
    @DisplayName("Should return list of appointments")
    void getAllAppointments_ShouldReturnListOfAppointments() {
        when(appointmentService.getAllAppointments()).thenReturn(testAppointmentsDto);

        ResponseEntity<List<AppointmentResponseDto>> response = appointmentController.getAllAppointments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testAppointmentsDto, response.getBody());
        verify(appointmentService).getAllAppointments();
    }

    @Test
    @DisplayName("Should return appointment by ID")
    void getAppointmentById_ShouldReturnAppointment() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(testAppointmentDto);

        ResponseEntity<AppointmentResponseDto> response = appointmentController.getAppointmentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testAppointmentDto, response.getBody());
        verify(appointmentService).getAppointmentById(1L);
    }

    @Test
    @DisplayName("Should create appointment and return CREATED status")
    void createAppointment_ShouldReturnCreatedAppointment() {
        when(appointmentService.createAppointment(any(CreateAppointmentDto.class))).thenReturn(testAppointmentDto);

        ResponseEntity<AppointmentResponseDto> response = appointmentController.createAppointment(createAppointmentDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testAppointmentDto, response.getBody());
        verify(appointmentService).createAppointment(createAppointmentDto);
    }

    @Test
    @DisplayName("Should update appointment and return updated appointment")
    void updateAppointment_ShouldReturnUpdatedAppointment() {
        when(appointmentService.updateAppointment(eq(1L), any(UpdateAppointmentDto.class))).thenReturn(testAppointmentDto);

        ResponseEntity<AppointmentResponseDto> response = appointmentController.updateAppointment(1L, updateAppointmentDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testAppointmentDto, response.getBody());
        verify(appointmentService).updateAppointment(1L, updateAppointmentDto);
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
