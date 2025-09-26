package com.hospital.api.service;

import com.hospital.api.entity.Doctor;
import com.hospital.api.exception.notfound.ResourceNotFoundException;
import com.hospital.api.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> ResourceNotFoundException.Builder.doctor(String.valueOf(doctorId)));
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long doctorId, Doctor doctorDetails) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> ResourceNotFoundException.Builder.doctor(String.valueOf(doctorId)));

        doctor.setFirstName(doctorDetails.getFirstName());
        doctor.setLastName(doctorDetails.getLastName());
        doctor.setEmail(doctorDetails.getEmail());
        doctor.setSpecialty(doctorDetails.getSpecialty());

        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> ResourceNotFoundException.Builder.doctor(String.valueOf(doctorId)));

        doctorRepository.delete(doctor);
    }

}
