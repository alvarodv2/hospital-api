package com.hospital.api.repository;

import com.hospital.api.entity.Appointment;
import com.hospital.api.entity.Doctor;
import com.hospital.api.entity.Patient;
import com.hospital.api.entity.Prescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class PrescriptionRepositoryTest {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    void setup() {
        doctor = Doctor.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .specialty("General")
                .build();
        doctor = doctorRepository.save(doctor);

        patient = Patient.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@example.com")
                .numberPhone("123456789")
                .build();
        patient = patientRepository.save(patient);
    }

    @Test
    void testSaveAndFindById() {
        Appointment appointment = Appointment.builder()
                .appointmentDate(LocalDateTime.now())
                .notes("Test appointment")
                .doctor(doctor)
                .patient(patient)
                .build();
        appointment = appointmentRepository.save(appointment);

        Prescription prescription = new Prescription();
        prescription.setMedication("Paracetamol");
        prescription.setInstructions("Take twice a day");
        prescription.setAppointment(appointment);

        Prescription saved = prescriptionRepository.save(prescription);

        Optional<Prescription> found = prescriptionRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getMedication()).isEqualTo("Paracetamol");
        assertThat(found.get().getInstructions()).isEqualTo("Take twice a day");
    }

    @Test
    void testDelete() {
        Appointment appointment = Appointment.builder()
                .appointmentDate(LocalDateTime.now())
                .notes("Test appointment for delete")
                .doctor(doctor)
                .patient(patient)
                .build();
        appointment = appointmentRepository.save(appointment);

        Prescription prescription = new Prescription();
        prescription.setMedication("Ibuprofen");
        prescription.setInstructions("Take after meals");
        prescription.setAppointment(appointment);

        Prescription saved = prescriptionRepository.save(prescription);

        prescriptionRepository.deleteById(saved.getId());

        assertThat(prescriptionRepository.findById(saved.getId())).isEmpty();
    }
}
