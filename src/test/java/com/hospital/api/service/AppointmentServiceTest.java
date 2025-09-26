package com.hospital.api.service;

import com.hospital.api.entity.*;
import com.hospital.api.exception.notfound.AppointmentNotFoundException;
import com.hospital.api.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    private Appointment appointment;
    private Doctor doctor;
    private Patient patient;
    private Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doctor = Doctor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Perez")
                .email("john.perez@hospital.com")
                .specialty("Cardiology")
                .appointments(new ArrayList<>())
                .build();

        patient = Patient.builder()
                .id(1L)
                .firstName("Anna")
                .lastName("Lopez")
                .email("anna.lopez@example.com")
                .build();

        room = Room.builder()
                .id(2L)
                .name("Office 102")
                .location("Second floor")
                .build();

        appointment = Appointment.builder()
                .id(1L)
                .appointmentDate(LocalDateTime.now())
                .notes("Initial consultation")
                .doctor(doctor)
                .patient(patient)
                .room(room)
                .prescriptions(new ArrayList<>())
                .build();
    }

    @Test
    void getAllAppointments_ReturnsList() {
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        List<Appointment> appointments = appointmentService.getAllAppointments();

        assertNotNull(appointments);
        assertEquals(1, appointments.size());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    void getAppointmentById_WhenExists_ReturnsAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Appointment found = appointmentService.getAppointmentById(1L);

        assertNotNull(found);
        assertEquals("John", found.getDoctor().getFirstName());
        verify(appointmentRepository, times(1)).findById(1L);
    }

    @Test
    void getAppointmentById_WhenNotExists_ThrowsException() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.getAppointmentById(1L));
        verify(appointmentRepository, times(1)).findById(1L);
    }

    @Test
    void createAppointment_ReturnsSavedAppointment() {
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment saved = appointmentService.createAppointment(appointment);

        assertNotNull(saved);
        assertEquals("Initial consultation", saved.getNotes());
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    void updateAppointment_WhenExists_ReturnsUpdatedAppointment() {
        Doctor newDoctor = Doctor.builder()
                .id(2L)
                .firstName("Mary")
                .lastName("Gomez")
                .email("mary.gomez@hospital.com")
                .specialty("Pediatrics")
                .appointments(new ArrayList<>())
                .build();

        Patient newPatient = Patient.builder()
                .id(2L)
                .firstName("Charles")
                .lastName("Martinez")
                .email("charles.martinez@example.com")
                .build();

        Room newRoom = Room.builder()
                .id(2L)
                .name("Office 102")
                .location("Second floor")
                .build();

        Appointment updatedDetails = Appointment.builder()
                .appointmentDate(LocalDateTime.now().plusDays(1))
                .notes("Appointment update")
                .doctor(newDoctor)
                .patient(newPatient)
                .room(newRoom)
                .prescriptions(new ArrayList<>())
                .build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(updatedDetails);

        Appointment updated = appointmentService.updateAppointment(1L, updatedDetails);

        assertEquals("Appointment update", updated.getNotes());
        assertEquals("Mary", updated.getDoctor().getFirstName());
        assertEquals("Charles", updated.getPatient().getFirstName());
        assertEquals("Office 102", updated.getRoom().getName());
        verify(appointmentRepository, times(1)).findById(1L);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void deleteAppointment_WhenExists_DeletesAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        doNothing().when(appointmentRepository).delete(appointment);

        assertDoesNotThrow(() -> appointmentService.deleteAppointment(1L));
        verify(appointmentRepository, times(1)).findById(1L);
        verify(appointmentRepository, times(1)).delete(appointment);
    }

    @Test
    void deleteAppointment_WhenNotExists_ThrowsException() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.deleteAppointment(1L));
        verify(appointmentRepository, times(1)).findById(1L);
        verify(appointmentRepository, never()).delete(any());
    }

}
