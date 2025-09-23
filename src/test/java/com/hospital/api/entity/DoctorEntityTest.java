package com.hospital.api.entity;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DoctorEntityTest {

    @Test
    void testBuilderAndGetters() {
        Doctor doctor = Doctor.builder()
                .firstName("Luis")
                .lastName("Martínez")
                .email("luis@example.com")
                .specialty("Cardiology")
                .build();

        assertEquals("Luis", doctor.getFirstName());
        assertEquals("Martínez", doctor.getLastName());
        assertEquals("luis@example.com", doctor.getEmail());
        assertEquals("Cardiology", doctor.getSpecialty());
    }

    @Test
    void testAppointmentsAssignment() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Ana");

        Appointment appt1 = new Appointment();
        Appointment appt2 = new Appointment();

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appt1);
        appointments.add(appt2);

        doctor.setAppointments(appointments);

        assertEquals(2, doctor.getAppointments().size());
        assertTrue(doctor.getAppointments().contains(appt1));
    }
}
