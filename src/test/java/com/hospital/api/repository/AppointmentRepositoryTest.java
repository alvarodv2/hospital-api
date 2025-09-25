package com.hospital.api.repository;

import com.hospital.api.entity.Appointment;
import com.hospital.api.entity.Doctor;
import com.hospital.api.entity.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void findByAppointmentDate_shouldReturnAppointments() {
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 10, 30);

        Doctor doctor = Doctor.builder()
                .firstName("Luis")
                .lastName("Martínez")
                .email("luis@hospital.com")
                .specialty("Cardiology")
                .build();
        doctor = doctorRepository.save(doctor);

        Patient patient = Patient.builder()
                .firstName("Ana")
                .lastName("Gomez")
                .email("ana@correo.com")
                .build();
        patient = patientRepository.save(patient);

        Appointment appointment = Appointment.builder()
                .appointmentDate(date)
                .notes("Routine check-up")
                .doctor(doctor)
                .patient(patient)
                .build();
        appointmentRepository.save(appointment);

        List<Appointment> result = appointmentRepository.findByAppointmentDate(date);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDoctor().getFirstName()).isEqualTo("Luis");
        assertThat(result.get(0).getPatient().getFirstName()).isEqualTo("Ana");
    }

    @Test
    void findByAppointmentDateBetween_shouldReturnAppointmentsInRange() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime end   = LocalDateTime.of(2025, 1, 2, 23, 59);

        Doctor doctor = doctorRepository.save(Doctor.builder()
                .firstName("Mario")
                .lastName("Perez")
                .email("mario@hospital.com")
                .specialty("Pediatrics")
                .build());

        Patient patient = patientRepository.save(Patient.builder()
                .firstName("Carla")
                .lastName("Diaz")
                .email("carla@correo.com")
                .build());

        appointmentRepository.save(Appointment.builder()
                .appointmentDate(LocalDateTime.of(2025, 1, 1, 14, 0))
                .notes("Pediatric consultation")
                .doctor(doctor)
                .patient(patient)
                .build());

        List<Appointment> result = appointmentRepository.findByAppointmentDateBetween(start, end);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getNotes()).isEqualTo("Pediatric consultation");
    }
}
