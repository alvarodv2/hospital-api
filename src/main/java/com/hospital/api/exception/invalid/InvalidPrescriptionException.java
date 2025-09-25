package com.hospital.api.exception.invalid;

public class InvalidPrescriptionException extends InvalidException {

    public InvalidPrescriptionException(String invalidValue, String additionalMessage, String context) {
        super(ErrorCode.INVALID_PRESCRIPTION, "Prescription", invalidValue, additionalMessage, context);
    }

    public InvalidPrescriptionException(String invalidValue) {
        super(ErrorCode.INVALID_PRESCRIPTION, "Prescription", invalidValue);
    }

    public InvalidPrescriptionException(String exceptionMessage, boolean legacy) {
        super(exceptionMessage);
    }
}
