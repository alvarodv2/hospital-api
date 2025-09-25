package com.hospital.api.repository;

import com.hospital.api.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findPatientByLastName(String lastName);

}
