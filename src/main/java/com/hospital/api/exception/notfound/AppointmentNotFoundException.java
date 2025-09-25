package com.hospital.api.exception.notfound;

public class AppointmentNotFoundException extends ResourceNotFoundException {

    public AppointmentNotFoundException(String appointmentId, String additionalMessage, String context) {
        super(ErrorCode.APPOINTMENT_NOT_FOUND, "Appointment", appointmentId, additionalMessage, context);
    }

    public AppointmentNotFoundException(String appointmentId) {
        super(ErrorCode.APPOINTMENT_NOT_FOUND, "Appointment", appointmentId);
    }

    public AppointmentNotFoundException(String exceptionMessage, boolean legacy) {
        super(exceptionMessage);
    }

}