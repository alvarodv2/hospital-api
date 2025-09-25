package com.hospital.api.exception.invalid;

public class InvalidAppointmentException extends InvalidException {

    public InvalidAppointmentException(String invalidValue, String additionalMessage, String context) {
        super(ErrorCode.INVALID_APPOINTMENT, "Appointment", invalidValue, additionalMessage, context);
    }

    public InvalidAppointmentException(String invalidValue) {
        super(ErrorCode.INVALID_APPOINTMENT, "Appointment", invalidValue);
    }

    public InvalidAppointmentException(String exceptionMessage, boolean legacy) {
        super(exceptionMessage);
    }
}
