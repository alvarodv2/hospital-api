package com.hospital.api.repository;

import com.hospital.api.entity.Patient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
public class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void findByLastName_shouldReturnPatient(){

        Patient patient = Patient.builder()
                .firstName("Eduardo")
                .lastName("Arenas")
                .email("example@hotmail.com")
                .numberPhone("690 233 147")
                .build();

        Patient saved = patientRepository.save(patient);

        assertThat(saved.getId()).isNotNull();

        List<Patient> result = patientRepository.findPatientByLastName("Arenas");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLastName()).isEqualTo("Arenas");

    }

    @Test
    void findByLastName_shouldReturnEmptyList_whenNoMatch() {
        List<Patient> result = patientRepository.findPatientByLastName("NonExistent");
        assertThat(result).isEmpty();
        log.info("Patient not found");
    }

    @Test
    void findByLastName_shouldReturnMultiplePatients() {
        patientRepository.save(Patient.builder().firstName("Juan").lastName("Lopez").email("juan@mail.com").numberPhone("123").build());
        patientRepository.save(Patient.builder().firstName("Ana").lastName("Lopez").email("ana@mail.com").numberPhone("456").build());

        List<Patient> result = patientRepository.findPatientByLastName("Lopez");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Patient::getFirstName).containsExactlyInAnyOrder("Juan", "Ana");
    }

}
