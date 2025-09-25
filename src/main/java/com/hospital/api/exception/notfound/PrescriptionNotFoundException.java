package com.hospital.api.exception.notfound;

public class PrescriptionNotFoundException extends ResourceNotFoundException {

    public PrescriptionNotFoundException(String prescriptionId, String additionalMessage, String context) {
        super(ErrorCode.MEDICAL_RECORD_NOT_FOUND, "Prescription", prescriptionId, additionalMessage, context);
    }

    public PrescriptionNotFoundException(String prescriptionId) {
        super(ErrorCode.MEDICAL_RECORD_NOT_FOUND, "Prescription", prescriptionId);
    }

    public PrescriptionNotFoundException(String exceptionMessage, boolean legacy) {
        super(exceptionMessage);
    }

}
