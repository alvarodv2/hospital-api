package com.hospital.api.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentEntityTest {

    @Test
    void testBuilderAndGetters() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 1, 10, 30);

        Patient patient = Patient.builder().firstName("Ana").lastName("Gomez").build();
        Doctor doctor = Doctor.builder().firstName("Luis").lastName("Martínez").build();
        Room room = Room.builder().name("Sala 1").location("Piso 2").build();

        Appointment appointment = Appointment.builder()
                .appointmentDate(dateTime)
                .notes("Consulta rutinaria")
                .patient(patient)
                .doctor(doctor)
                .room(room)
                .build();

        assertEquals(dateTime, appointment.getAppointmentDate());
        assertEquals("Consulta rutinaria", appointment.getNotes());
        assertEquals(patient, appointment.getPatient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(room, appointment.getRoom());
    }

    @Test
    void testPrescriptionsAssignment() {
        Appointment appointment = new Appointment();
        Prescription p1 = Prescription.builder().medication("Ibuprofeno").instructions("Tomar 1 cada 8h").build();
        Prescription p2 = Prescription.builder().medication("Paracetamol").instructions("Tomar 1 cada 6h").build();

        List<Prescription> prescriptions = new ArrayList<>();
        prescriptions.add(p1);
        prescriptions.add(p2);

        appointment.setPrescriptions(prescriptions);

        assertEquals(2, appointment.getPrescriptions().size());
        assertTrue(appointment.getPrescriptions().contains(p1));
        assertTrue(appointment.getPrescriptions().contains(p2));
    }
}
