package com.hospital.api.service;

import com.hospital.api.dto.CreatePatientDto;
import com.hospital.api.dto.PatientResponseDto;
import com.hospital.api.dto.UpdatePatientDto;
import com.hospital.api.entity.Patient;
import com.hospital.api.exception.notfound.ResourceNotFoundException;
import com.hospital.api.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient1;
    private Patient patient2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient1 = Patient.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .numberPhone("123456789")
                .build();

        patient2 = Patient.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .numberPhone("987654321")
                .build();
    }

    @Test
    void getAllPatients_ShouldReturnListOfPatientDtos() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient1, patient2));

        List<PatientResponseDto> patients = patientService.getAllPatients();

        assertThat(patients).hasSize(2);
        assertThat(patients.get(0).getId()).isEqualTo(1L);
        assertThat(patients.get(0).getFirstName()).isEqualTo("John");
        assertThat(patients.get(1).getId()).isEqualTo(2L);
        assertThat(patients.get(1).getFirstName()).isEqualTo("Jane");
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getPatientById_WhenExists_ShouldReturnPatientDto() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient1));

        PatientResponseDto found = patientService.getPatientById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getFirstName()).isEqualTo("John");
        assertThat(found.getLastName()).isEqualTo("Doe");
        assertThat(found.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(found.getNumberPhone()).isEqualTo("123456789");
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void getPatientById_WhenNotFound_ShouldThrowException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientById(1L));
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void createPatient_ShouldSaveAndReturnPatientDto() {
        CreatePatientDto createDto = new CreatePatientDto("John", "Doe", "john.doe@example.com", "123456789");

        when(patientRepository.save(any(Patient.class))).thenReturn(patient1);

        PatientResponseDto created = patientService.createPatient(createDto);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getFirstName()).isEqualTo("John");
        assertThat(created.getLastName()).isEqualTo("Doe");
        assertThat(created.getEmail()).isEqualTo("john.doe@example.com");
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_WhenExists_ShouldUpdateAndReturnPatientDto() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient1));
        when(patientRepository.save(any(Patient.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UpdatePatientDto updateDto = new UpdatePatientDto("Updated", "Name", "updated@example.com", "111222333");

        PatientResponseDto updated = patientService.updatePatient(1L, updateDto);

        assertThat(updated.getFirstName()).isEqualTo("Updated");
        assertThat(updated.getLastName()).isEqualTo("Name");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");
        assertThat(updated.getNumberPhone()).isEqualTo("111222333");

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_WithPartialData_ShouldOnlyUpdateProvidedFields() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient1));
        when(patientRepository.save(any(Patient.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UpdatePatientDto updateDto = new UpdatePatientDto();
        updateDto.setFirstName("Updated");
        updateDto.setEmail("updated@example.com");

        PatientResponseDto updated = patientService.updatePatient(1L, updateDto);

        assertThat(updated.getFirstName()).isEqualTo("Updated");
        assertThat(updated.getLastName()).isEqualTo("Doe");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");
        assertThat(updated.getNumberPhone()).isEqualTo("123456789");

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_WhenNotFound_ShouldThrowException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        UpdatePatientDto updateDto = new UpdatePatientDto();

        assertThrows(ResourceNotFoundException.class, () -> patientService.updatePatient(1L, updateDto));
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void deletePatient_WhenExists_ShouldDeletePatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient1));
        doNothing().when(patientRepository).delete(patient1);

        patientService.deletePatient(1L);

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).delete(patient1);
    }

    @Test
    void deletePatient_WhenNotFound_ShouldThrowException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.deletePatient(1L));
        verify(patientRepository, times(1)).findById(1L);
    }


}