package com.hospital.api.repository;

import com.hospital.api.entity.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DoctorRepositoryTests {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void findBySpecialty_shouldReturnDoctors() {
        Doctor doc = Doctor.builder()
                .firstName("Luis")
                .lastName("Martínez")
                .email("luis.martinez@hospital.com")
                .specialty("Cardiology")
                .build();

        Doctor saved = doctorRepository.save(doc);

        assertThat(saved.getId()).isNotNull();

        List<Doctor> result = doctorRepository.findBySpecialty("Cardiology");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSpecialty()).isEqualTo("Cardiology");
    }



}
