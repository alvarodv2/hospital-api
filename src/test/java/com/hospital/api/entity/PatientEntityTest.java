package com.hospital.api.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientEntityTest {

    @Test
    void testBuilderAndGetters() {
        Patient patient = Patient.builder()
                .firstName("Ana")
                .lastName("Gomez")
                .email("ana@example.com")
                .numberPhone("123456789")
                .build();

        assertEquals("Ana", patient.getFirstName());
        assertEquals("Gomez", patient.getLastName());
        assertEquals("ana@example.com", patient.getEmail());
        assertEquals("123456789", patient.getNumberPhone());
    }

    @Test
    void testAppointmentsAssignment() {
        Patient patient = new Patient();
        patient.setFirstName("Juan");

        Appointment appt1 = new Appointment();
        Appointment appt2 = new Appointment();

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appt1);
        appointments.add(appt2);

        patient.setAppointments(appointments);

        assertEquals(2, patient.getAppointments().size());
        assertTrue(patient.getAppointments().contains(appt1));
    }
}
