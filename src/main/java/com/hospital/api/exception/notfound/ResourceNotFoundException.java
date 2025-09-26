package com.hospital.api.exception.notfound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base exception for resources not found in the hospital API.
 * Provides advanced features such as automatic logging, error codes,
 * timestamps, and additional debugging context.
 *
 * @author alvarodv2
 * @version 1.0
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ResourceNotFoundException.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum ErrorCode {
        RESOURCE_NOT_FOUND("RESOURCE_404", "The requested resource does not exist"),
        APPOINTMENT_NOT_FOUND("APPOINTMENT_404", "The medical appointment was not found"),
        PATIENT_NOT_FOUND("PATIENT_404", "The patient was not found"),
        DOCTOR_NOT_FOUND("DOCTOR_404", "The doctor was not found"),
        MEDICAL_RECORD_NOT_FOUND("RECORD_404", "The medical record was not found");

        private final String code;
        private final String description;

        ErrorCode(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() { return code; }
        public String getDescription() { return description; }
    }

    private final ErrorCode errorCode;
    private final String resourceType;
    private final String resourceId;
    private final String timestamp;
    private final String requestContext;

    public ResourceNotFoundException(ErrorCode errorCode, String resourceType,
                                     String resourceId, String additionalMessage,
                                     String requestContext) {
        super(buildDetailedMessage(errorCode, resourceType, resourceId, additionalMessage));
        this.errorCode = errorCode;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.requestContext = requestContext;
        this.timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);

        logException();
    }

    public ResourceNotFoundException(ErrorCode errorCode, String resourceType, String resourceId) {
        this(errorCode, resourceType, resourceId, null, "API Request");
    }

    public ResourceNotFoundException(String message) {
        this(ErrorCode.RESOURCE_NOT_FOUND, "Unknown", "Unknown", message, "Legacy Call");
    }

    private static String buildDetailedMessage(ErrorCode errorCode, String resourceType,
                                               String resourceId, String additionalMessage) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("[%s] %s", errorCode.getCode(), errorCode.getDescription()));

        if (resourceType != null && !resourceType.isEmpty()) {
            message.append(String.format(" | Type: %s", resourceType));
        }

        if (resourceId != null && !resourceId.isEmpty()) {
            message.append(String.format(" | ID: %s", resourceId));
        }

        if (additionalMessage != null && !additionalMessage.isEmpty()) {
            message.append(String.format(" | Details: %s", additionalMessage));
        }

        return message.toString();
    }

    private void logException() {
        logger.warn("Resource Not Found Exception occurred: {} | Resource Type: {} | Resource ID: {} | Context: {} | Timestamp: {}",
                getMessage(), resourceType, resourceId, requestContext, timestamp);

        if (logger.isDebugEnabled()) {
            logger.debug("Stack trace for ResourceNotFoundException", this);
        }
    }

    public ErrorCode getErrorCode() { return errorCode; }
    public String getResourceType() { return resourceType; }
    public String getResourceId() { return resourceId; }
    public String getTimestamp() { return timestamp; }
    public String getRequestContext() { return requestContext; }

    public ApiErrorResponse toApiErrorResponse() {
        return new ApiErrorResponse(
                errorCode.getCode(),
                getMessage(),
                resourceType,
                resourceId,
                timestamp,
                requestContext
        );
    }

    public static class ApiErrorResponse {
        private final String errorCode;
        private final String message;
        private final String resourceType;
        private final String resourceId;
        private final String timestamp;
        private final String context;

        public ApiErrorResponse(String errorCode, String message, String resourceType,
                                String resourceId, String timestamp, String context) {
            this.errorCode = errorCode;
            this.message = message;
            this.resourceType = resourceType;
            this.resourceId = resourceId;
            this.timestamp = timestamp;
            this.context = context;
        }

        public String getErrorCode() { return errorCode; }
        public String getMessage() { return message; }
        public String getResourceType() { return resourceType; }
        public String getResourceId() { return resourceId; }
        public String getTimestamp() { return timestamp; }
        public String getContext() { return context; }
    }

    public static class Builder {
        public static ResourceNotFoundException appointment(String appointmentId) {
            return new ResourceNotFoundException(ErrorCode.APPOINTMENT_NOT_FOUND, "Appointment", appointmentId);
        }

        public static ResourceNotFoundException patient(String patientId) {
            return new ResourceNotFoundException(ErrorCode.PATIENT_NOT_FOUND, "Patient", patientId);
        }

        public static ResourceNotFoundException doctor(String doctorId) {
            return new ResourceNotFoundException(ErrorCode.DOCTOR_NOT_FOUND, "Doctor", doctorId);
        }

        public static ResourceNotFoundException medicalRecord(String recordId) {
            return new ResourceNotFoundException(ErrorCode.MEDICAL_RECORD_NOT_FOUND, "MedicalRecord", recordId);
        }
    }

}
