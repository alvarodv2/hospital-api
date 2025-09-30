package com.hospital.api.service;

import com.hospital.api.dto.CreateDoctorDto;
import com.hospital.api.dto.DoctorResponseDto;
import com.hospital.api.dto.UpdateDoctorDto;
import com.hospital.api.entity.Doctor;
import com.hospital.api.exception.notfound.ResourceNotFoundException;
import com.hospital.api.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor doctor1;
    private Doctor doctor2;
    private DoctorResponseDto doctorResponseDto1;
    private DoctorResponseDto doctorResponseDto2;
    private CreateDoctorDto createDoctorDto;
    private UpdateDoctorDto updateDoctorDto;

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

        doctorResponseDto1 = new DoctorResponseDto(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                "Cardiology"
        );

        doctorResponseDto2 = new DoctorResponseDto(
                2L,
                "Jane",
                "Smith",
                "jane.smith@example.com",
                "Dermatology"
        );

        createDoctorDto = new CreateDoctorDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "Cardiology"
        );

        updateDoctorDto = new UpdateDoctorDto(
                "Updated",
                "Name",
                "updated@example.com",
                "Neurology"
        );
    }

    @Test
    void getAllDoctors_ShouldReturnListOfDoctors() {
        when(doctorRepository.findAll()).thenReturn(Arrays.asList(doctor1, doctor2));

        List<DoctorResponseDto> doctors = doctorService.getAllDoctors();

        assertThat(doctors).hasSize(2)
                .extracting("id", "firstName", "lastName", "email", "specialty")
                .containsExactly(
                        tuple(1L, "John", "Doe", "john.doe@example.com", "Cardiology"),
                        tuple(2L, "Jane", "Smith", "jane.smith@example.com", "Dermatology")
                );
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void getDoctorById_WhenExists_ShouldReturnDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor1));

        DoctorResponseDto found = doctorService.getDoctorById(1L);

        assertThat(found)
                .extracting("id", "firstName", "lastName", "email", "specialty")
                .containsExactly(1L, "John", "Doe", "john.doe@example.com", "Cardiology");
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
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor1);

        DoctorResponseDto created = doctorService.createDoctor(createDoctorDto);

        assertThat(created)
                .extracting("id", "firstName", "lastName", "email", "specialty")
                .containsExactly(1L, "John", "Doe", "john.doe@example.com", "Cardiology");
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void updateDoctor_WhenExists_ShouldUpdateAndReturnDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor1));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(
                Doctor.builder()
                        .id(1L)
                        .firstName("Updated")
                        .lastName("Name")
                        .email("updated@example.com")
                        .specialty("Neurology")
                        .build()
        );

        DoctorResponseDto updated = doctorService.updateDoctor(1L, updateDoctorDto);

        assertThat(updated)
                .extracting("firstName", "lastName", "email", "specialty")
                .containsExactly("Updated", "Name", "updated@example.com", "Neurology");

        verify(doctorRepository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void updateDoctor_WhenNotFound_ShouldThrowException() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.updateDoctor(1L, updateDoctorDto));
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
