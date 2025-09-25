package com.hospital.api.exception.notfound;

public class DoctorNotFoundException extends ResourceNotFoundException {

    public DoctorNotFoundException(String doctorId, String additionalMessage, String context) {
        super(ErrorCode.DOCTOR_NOT_FOUND, "Doctor", doctorId, additionalMessage, context);
    }

    public DoctorNotFoundException(String doctorId) {
        super(ErrorCode.DOCTOR_NOT_FOUND, "Doctor", doctorId);
    }

    public DoctorNotFoundException(String exceptionMessage, boolean legacy) {
        super(exceptionMessage);
    }

}
