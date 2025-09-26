package com.hospital.api.service;

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
    void getAllPatients_ShouldReturnListOfPatients() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient1, patient2));

        List<Patient> patients = patientService.getAllPatients();

        assertThat(patients).hasSize(2).contains(patient1, patient2);
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getPatientById_WhenExists_ShouldReturnPatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient1));

        Patient found = patientService.getPatientById(1L);

        assertThat(found).isEqualTo(patient1);
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void getPatientById_WhenNotFound_ShouldThrowException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientById(1L));
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void createPatient_ShouldSaveAndReturnPatient() {
        when(patientRepository.save(patient1)).thenReturn(patient1);

        Patient created = patientService.createPatient(patient1);

        assertThat(created).isEqualTo(patient1);
        verify(patientRepository, times(1)).save(patient1);
    }

    @Test
    void updatePatient_WhenExists_ShouldUpdateAndReturnPatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient1));
        when(patientRepository.save(any(Patient.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Patient updates = Patient.builder()
                .firstName("Updated")
                .lastName("Name")
                .email("updated@example.com")
                .numberPhone("111222333")
                .build();

        Patient updated = patientService.updatePatient(1L, updates);

        assertThat(updated.getFirstName()).isEqualTo("Updated");
        assertThat(updated.getLastName()).isEqualTo("Name");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");
        assertThat(updated.getNumberPhone()).isEqualTo("111222333");

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_WhenNotFound_ShouldThrowException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        Patient updates = new Patient();

        assertThrows(ResourceNotFoundException.class, () -> patientService.updatePatient(1L, updates));
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
