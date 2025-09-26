package com.hospital.api.service;

import com.hospital.api.entity.Appointment;
import com.hospital.api.exception.notfound.AppointmentNotFoundException;
import com.hospital.api.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointments(){
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Long appointmentId){
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(String.valueOf(appointmentId)));
    }

    public Appointment createAppointment (Appointment appointment){
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long id, Appointment appointmentDetails){
        Appointment appointment = getAppointmentById(id);

        appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
        appointment.setNotes(appointmentDetails.getNotes());
        appointment.setDoctor(appointmentDetails.getDoctor());
        appointment.setPatient(appointmentDetails.getPatient());
        appointment.setRoom(appointmentDetails.getRoom());
        appointment.setPrescriptions(appointmentDetails.getPrescriptions());

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long appointmentId){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                        .orElseThrow(() -> new AppointmentNotFoundException(String.valueOf(appointmentId)));

        appointmentRepository.delete(appointment);
    }

}
