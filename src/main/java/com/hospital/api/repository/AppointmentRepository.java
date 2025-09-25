package com.hospital.api.repository;


import com.hospital.api.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository <Appointment, Long> {

    List<Appointment> findByAppointmentDate(LocalDateTime appointmentDate);
    List<Appointment> findByAppointmentDateBetween (LocalDateTime start, LocalDateTime end);

}
