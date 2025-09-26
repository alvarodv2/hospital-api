package com.hospital.api.service;

import com.hospital.api.entity.Doctor;
import com.hospital.api.exception.notfound.ResourceNotFoundException;
import com.hospital.api.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor doctor1;
    private Doctor doctor2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doctor1 = Doctor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .specialty("Cardiology")
                .build();

        doctor2 = Doctor.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .specialty("Dermatology")
                .build();
    }

    @Test
    void getAllDoctors_ShouldReturnListOfDoctors() {
        when(doctorRepository.findAll()).thenReturn(Arrays.asList(doctor1, doctor2));

        List<Doctor> doctors = doctorService.getAllDoctors();

        assertThat(doctors).hasSize(2).contains(doctor1, doctor2);
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void getDoctorById_WhenExists_ShouldReturnDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor1));

        Doctor found = doctorService.getDoctorById(1L);

        assertThat(found).isEqualTo(doctor1);
        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
    void getDoctorById_WhenNotFound_ShouldThrowException() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.getDoctorById(1L));
        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
    void createDoctor_ShouldSaveAndReturnDoctor() {
        when(doctorRepository.save(doctor1)).thenReturn(doctor1);

        Doctor created = doctorService.createDoctor(doctor1);

        assertThat(created).isEqualTo(doctor1);
        verify(doctorRepository, times(1)).save(doctor1);
    }

    @Test
    void updateDoctor_WhenExists_ShouldUpdateAndReturnDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor1));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor1);

        Doctor updates = Doctor.builder()
                .firstName("Updated")
                .lastName("Name")
                .email("updated@example.com")
                .specialty("Neurology")
                .build();

        Doctor updated = doctorService.updateDoctor(1L, updates);

        assertThat(updated.getFirstName()).isEqualTo("Updated");
        assertThat(updated.getLastName()).isEqualTo("Name");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");
        assertThat(updated.getSpecialty()).isEqualTo("Neurology");

        verify(doctorRepository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).save(doctor1);
    }

    @Test
    void updateDoctor_WhenNotFound_ShouldThrowException() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        Doctor updates = new Doctor();

        assertThrows(ResourceNotFoundException.class, () -> doctorService.updateDoctor(1L, updates));
        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
    void deleteDoctor_WhenExists_ShouldDeleteDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor1));
        doNothing().when(doctorRepository).delete(doctor1);

        doctorService.deleteDoctor(1L);

        verify(doctorRepository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).delete(doctor1);
    }

    @Test
    void deleteDoctor_WhenNotFound_ShouldThrowException() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.deleteDoctor(1L));
        verify(doctorRepository, times(1)).findById(1L);
    }


}
