package com.hospital.api.service;

import com.hospital.api.dto.AppointmentResponseDto;
import com.hospital.api.dto.CreateAppointmentDto;
import com.hospital.api.dto.UpdateAppointmentDto;
import com.hospital.api.entity.Appointment;
import com.hospital.api.entity.Doctor;
import com.hospital.api.entity.Patient;
import com.hospital.api.entity.Room;
import com.hospital.api.exception.notfound.AppointmentNotFoundException;
import com.hospital.api.exception.notfound.ResourceNotFoundException;
import com.hospital.api.repository.AppointmentRepository;
import com.hospital.api.repository.DoctorRepository;
import com.hospital.api.repository.PatientRepository;
import com.hospital.api.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private RoomRepository roomRepository;

    private Appointment appointment;
    private Doctor doctor;
    private Patient patient;
    private Room room;
    private CreateAppointmentDto createAppointmentDto;
    private UpdateAppointmentDto updateAppointmentDto;

    @BeforeEach
    void setUp() {
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
                .id(1L)
                .name("Room 101")
                .location("First floor")
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

        createAppointmentDto = new CreateAppointmentDto();
        createAppointmentDto.setAppointmentDate(LocalDateTime.now());
        createAppointmentDto.setNotes("Initial consultation");
        createAppointmentDto.setDoctorId(1L);
        createAppointmentDto.setPatientId(1L);
        createAppointmentDto.setRoomId(1L);

        updateAppointmentDto = new UpdateAppointmentDto();
        updateAppointmentDto.setAppointmentDate(LocalDateTime.now().plusDays(1));
        updateAppointmentDto.setNotes("Updated consultation");
        updateAppointmentDto.setDoctorId(2L);
        updateAppointmentDto.setPatientId(2L);
        updateAppointmentDto.setRoomId(2L);
    }

    @Test
    void getAllAppointments_ReturnsList() {
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        List<AppointmentResponseDto> appointments = appointmentService.getAllAppointments();

        assertNotNull(appointments);
        assertEquals(1, appointments.size());
        assertEquals(appointment.getId(), appointments.get(0).getId());
        assertEquals(appointment.getNotes(), appointments.get(0).getNotes());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    void getAppointmentById_WhenExists_ReturnsAppointmentDto() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        AppointmentResponseDto found = appointmentService.getAppointmentById(1L);

        assertNotNull(found);
        assertEquals(appointment.getId(), found.getId());
        assertEquals(appointment.getNotes(), found.getNotes());
        assertEquals(doctor.getFirstName() + " " + doctor.getLastName(), found.getDoctor().getFullName());
        verify(appointmentRepository, times(1)).findById(1L);
    }

    @Test
    void getAppointmentById_WhenNotExists_ThrowsException() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.getAppointmentById(1L));
        verify(appointmentRepository, times(1)).findById(1L);
    }

    @Test
    void createAppointment_ReturnsSavedAppointmentDto() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentResponseDto saved = appointmentService.createAppointment(createAppointmentDto);

        assertNotNull(saved);
        assertEquals(appointment.getId(), saved.getId());
        assertEquals(appointment.getNotes(), saved.getNotes());
        assertEquals(doctor.getFirstName() + " " + doctor.getLastName(), saved.getDoctor().getFullName());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void updateAppointment_WhenExists_ReturnsUpdatedAppointmentDto() {
        Doctor newDoctor = Doctor.builder()
                .id(2L)
                .firstName("Mary")
                .lastName("Gomez")
                .email("mary.gomez@hospital.com")
                .specialty("Pediatrics")
                .appointments(new ArrayList<>())
                .build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(newDoctor));
        when(patientRepository.findById(2L)).thenReturn(Optional.of(Patient.builder().id(2L).firstName("Charles").lastName("Martinez").build()));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(Room.builder().id(2L).name("Office 102").build()));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
            Appointment savedAppointment = invocation.getArgument(0);
            savedAppointment.setId(1L);
            return savedAppointment;
        });

        AppointmentResponseDto updated = appointmentService.updateAppointment(1L, updateAppointmentDto);

        assertNotNull(updated);
        assertEquals("Updated consultation", updated.getNotes());
        assertEquals("Mary Gomez", updated.getDoctor().getFullName());
        verify(appointmentRepository, times(1)).findById(1L);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void deleteAppointment_WhenExists_DeletesAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        doNothing().when(appointmentRepository).delete(any(Appointment.class));

        appointmentService.deleteAppointment(1L);

        verify(appointmentRepository, times(1)).findById(1L);
        verify(appointmentRepository, times(1)).delete(appointment);
    }

    @Test
    void deleteAppointment_WhenNotExists_ThrowsException() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.deleteAppointment(1L));
        verify(appointmentRepository, times(1)).findById(1L);
        verify(appointmentRepository, never()).delete(any(Appointment.class));
    }

    @Test
    void createAppointment_WhenDoctorNotFound_ThrowsException() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.createAppointment(createAppointmentDto));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void createAppointment_WhenPatientNotFound_ThrowsException() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.createAppointment(createAppointmentDto));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void createAppointment_WhenRoomNotFound_ThrowsException() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.createAppointment(createAppointmentDto));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }


}
