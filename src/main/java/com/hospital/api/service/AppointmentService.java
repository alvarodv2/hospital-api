package com.hospital.api.service;

import com.hospital.api.dto.AppointmentResponseDto;
import com.hospital.api.dto.CreateAppointmentDto;
import com.hospital.api.dto.UpdateAppointmentDto;
import com.hospital.api.dto.summary.DoctorSummaryDto;
import com.hospital.api.dto.summary.PatientSummaryDto;
import com.hospital.api.dto.summary.RoomSummaryDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final RoomRepository roomRepository;

    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AppointmentResponseDto getAppointmentById(Long appointmentId) {
        return convertToDto(appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(String.valueOf(appointmentId))));
    }

    public AppointmentResponseDto createAppointment(CreateAppointmentDto createAppointmentDto) {
        Appointment appointment = new Appointment();
        updateAppointmentFromDto(appointment, createAppointmentDto);
        return convertToDto(appointmentRepository.save(appointment));
    }

    public AppointmentResponseDto updateAppointment(Long id, UpdateAppointmentDto updateAppointmentDto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(String.valueOf(id)));

        if (updateAppointmentDto.getAppointmentDate() != null) {
            appointment.setAppointmentDate(updateAppointmentDto.getAppointmentDate());
        }
        if (updateAppointmentDto.getNotes() != null) {
            appointment.setNotes(updateAppointmentDto.getNotes());
        }
        if (updateAppointmentDto.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(updateAppointmentDto.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + updateAppointmentDto.getDoctorId()));
            appointment.setDoctor(doctor);
        }
        if (updateAppointmentDto.getPatientId() != null) {
            Patient patient = patientRepository.findById(updateAppointmentDto.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + updateAppointmentDto.getPatientId()));
            appointment.setPatient(patient);
        }
        if (updateAppointmentDto.getRoomId() != null) {
            Room room = roomRepository.findById(updateAppointmentDto.getRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + updateAppointmentDto.getRoomId()));
            appointment.setRoom(room);
        }

        return convertToDto(appointmentRepository.save(appointment));
    }

    public void deleteAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(String.valueOf(appointmentId)));
        appointmentRepository.delete(appointment);
    }

    private AppointmentResponseDto convertToDto(Appointment appointment) {
        return new AppointmentResponseDto(
                appointment.getId(),
                appointment.getAppointmentDate(),
                appointment.getNotes(),
                new PatientSummaryDto(
                        appointment.getPatient().getId(),
                        appointment.getPatient().getFirstName(),
                        appointment.getPatient().getLastName()
                ),
                new DoctorSummaryDto(
                        appointment.getDoctor().getId(),
                        appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName(),
                        appointment.getDoctor().getSpecialty()
                ),
                new RoomSummaryDto(
                        appointment.getRoom().getId(),
                        appointment.getRoom().getName()
                )
        );
    }

    private void updateAppointmentFromDto(Appointment appointment, CreateAppointmentDto createAppointmentDto) {
        appointment.setAppointmentDate(createAppointmentDto.getAppointmentDate());
        appointment.setNotes(createAppointmentDto.getNotes());

        Doctor doctor = doctorRepository.findById(createAppointmentDto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + createAppointmentDto.getDoctorId()));
        appointment.setDoctor(doctor);

        Patient patient = patientRepository.findById(createAppointmentDto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + createAppointmentDto.getPatientId()));
        appointment.setPatient(patient);

        Room room = roomRepository.findById(createAppointmentDto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + createAppointmentDto.getRoomId()));
        appointment.setRoom(room);
    }

}
