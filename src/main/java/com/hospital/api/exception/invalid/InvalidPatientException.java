package com.hospital.api.exception.invalid;

public class InvalidPatientException extends InvalidException {

    public InvalidPatientException(String invalidValue, String additionalMessage, String context) {
        super(ErrorCode.INVALID_PATIENT, "Patient", invalidValue, additionalMessage, context);
    }

    public InvalidPatientException(String invalidValue) {
        super(ErrorCode.INVALID_PATIENT, "Patient", invalidValue);
    }

    public InvalidPatientException(String exceptionMessage, boolean legacy) {
        super(exceptionMessage);
    }
}
