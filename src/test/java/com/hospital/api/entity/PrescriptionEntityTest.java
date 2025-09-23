package com.hospital.api.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrescriptionEntityTest {

    @Test
    void testBuilderAndGetters() {
        Appointment appointment = new Appointment();

        Prescription prescription = Prescription.builder()
                .medication("Ibuprofeno")
                .instructions("Tomar 1 cada 8h")
                .appointment(appointment)
                .build();

        assertEquals("Ibuprofeno", prescription.getMedication());
        assertEquals("Tomar 1 cada 8h", prescription.getInstructions());
        assertEquals(appointment, prescription.getAppointment());
    }
}
