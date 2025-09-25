package com.hospital.api.exception.notfound;

public class PatientNotFoundException extends ResourceNotFoundException {

    public PatientNotFoundException(String patientId, String additionalMessage, String context) {
        super(ErrorCode.PATIENT_NOT_FOUND, "Patient", patientId, additionalMessage, context);
    }

    public PatientNotFoundException(String patientId) {
        super(ErrorCode.PATIENT_NOT_FOUND, "Patient", patientId);
    }

    public PatientNotFoundException(String exceptionMessage, boolean legacy) {
        super(exceptionMessage);
    }

}
