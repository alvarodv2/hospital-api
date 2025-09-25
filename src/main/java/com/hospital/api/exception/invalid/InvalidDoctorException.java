package com.hospital.api.exception.invalid;

public class InvalidDoctorException extends InvalidException {

    public InvalidDoctorException(String invalidValue, String additionalMessage, String context) {
        super(ErrorCode.INVALID_DOCTOR, "Doctor", invalidValue, additionalMessage, context);
    }

    public InvalidDoctorException(String invalidValue) {
        super(ErrorCode.INVALID_DOCTOR, "Doctor", invalidValue);
    }

    public InvalidDoctorException(String exceptionMessage, boolean legacy) {
        super(exceptionMessage);
    }
}
