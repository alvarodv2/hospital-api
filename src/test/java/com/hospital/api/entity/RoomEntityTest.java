package com.hospital.api.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomEntityTest {

    @Test
    void testBuilderAndGetters() {
        Room room = Room.builder()
                .name("Consultorio A")
                .location("Piso 1")
                .build();

        assertEquals("Consultorio A", room.getName());
        assertEquals("Piso 1", room.getLocation());
    }

    @Test
    void testAppointmentsAssignment() {
        Room room = new Room();
        Appointment appt1 = new Appointment();
        Appointment appt2 = new Appointment();

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appt1);
        appointments.add(appt2);

        room.setAppointments(appointments);

        assertEquals(2, room.getAppointments().size());
        assertTrue(room.getAppointments().contains(appt1));
        assertTrue(room.getAppointments().contains(appt2));
    }
}
