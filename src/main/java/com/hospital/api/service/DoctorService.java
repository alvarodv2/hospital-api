package com.hospital.api.service;

import com.hospital.api.dto.CreateDoctorDto;
import com.hospital.api.dto.DoctorResponseDto;
import com.hospital.api.dto.UpdateDoctorDto;
import com.hospital.api.entity.Doctor;
import com.hospital.api.exception.notfound.ResourceNotFoundException;
import com.hospital.api.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public List<DoctorResponseDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DoctorResponseDto getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> ResourceNotFoundException.Builder.doctor(String.valueOf(doctorId)));
        return convertToDto(doctor);
    }

    public DoctorResponseDto createDoctor(CreateDoctorDto createDoctorDto) {
        Doctor doctor = new Doctor();
        updateDoctorFromDto(doctor, createDoctorDto);
        return convertToDto(doctorRepository.save(doctor));
    }

    public DoctorResponseDto updateDoctor(Long doctorId, UpdateDoctorDto updateDoctorDto) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> ResourceNotFoundException.Builder.doctor(String.valueOf(doctorId)));

        if (updateDoctorDto.getFirstName() != null) {
            doctor.setFirstName(updateDoctorDto.getFirstName());
        }
        if (updateDoctorDto.getLastName() != null) {
            doctor.setLastName(updateDoctorDto.getLastName());
        }
        if (updateDoctorDto.getEmail() != null) {
            doctor.setEmail(updateDoctorDto.getEmail());
        }
        if (updateDoctorDto.getSpecialty() != null) {
            doctor.setSpecialty(updateDoctorDto.getSpecialty());
        }

        return convertToDto(doctorRepository.save(doctor));
    }

    public void deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> ResourceNotFoundException.Builder.doctor(String.valueOf(doctorId)));
        doctorRepository.delete(doctor);
    }

    private DoctorResponseDto convertToDto(Doctor doctor) {
        return new DoctorResponseDto(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getEmail(),
                doctor.getSpecialty()
        );
    }

    private void updateDoctorFromDto(Doctor doctor, CreateDoctorDto createDoctorDto) {
        doctor.setFirstName(createDoctorDto.getFirstName());
        doctor.setLastName(createDoctorDto.getLastName());
        doctor.setEmail(createDoctorDto.getEmail());
        doctor.setSpecialty(createDoctorDto.getSpecialty());
    }

}
