package com.hospital.api.exception.invalid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base exception for invalid requests in the Hospital API.
 * Provides advanced functionality such as automatic logging, error codes,
 * timestamps, and additional context for debugging.
 *
 * Author: alvarodv2
 * Version: 1.0
 */
public class InvalidException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(InvalidException.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum ErrorCode {
        INVALID_INPUT("INVALID_400", "Invalid input provided"),
        INVALID_DATE_FORMAT("INVALID_400", "Invalid date format"),
        INVALID_ID("INVALID_400", "Invalid ID"),
        MISSING_REQUIRED_FIELD("INVALID_400", "Missing required field"),
        INVALID_APPOINTMENT("APPOINTMENT_400", "Invalid appointment data"),
        INVALID_DOCTOR("DOCTOR_400", "Invalid doctor data"),
        INVALID_PATIENT("PATIENT_400", "Invalid patient data"),
        INVALID_PRESCRIPTION("PRESCRIPTION_400", "Invalid prescription data"),
        INVALID_ROOM("ROOM_400", "Invalid room data");

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
    private final String fieldName;
    private final String invalidValue;
    private final String timestamp;
    private final String requestContext;

    public InvalidException(ErrorCode errorCode, String fieldName,
                            String invalidValue, String additionalMessage,
                            String requestContext) {
        super(buildDetailedMessage(errorCode, fieldName, invalidValue, additionalMessage));
        this.errorCode = errorCode;
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
        this.requestContext = requestContext;
        this.timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);

        logException();
    }

    public InvalidException(ErrorCode errorCode, String fieldName, String invalidValue) {
        this(errorCode, fieldName, invalidValue, null, "API Request");
    }

    public InvalidException(String message) {
        this(ErrorCode.INVALID_INPUT, "Unknown", "Unknown", message, "Legacy Call");
    }

    private static String buildDetailedMessage(ErrorCode errorCode, String fieldName,
                                               String invalidValue, String additionalMessage) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("[%s] %s", errorCode.getCode(), errorCode.getDescription()));

        if (fieldName != null && !fieldName.isEmpty()) {
            message.append(String.format(" | Field: %s", fieldName));
        }

        if (invalidValue != null && !invalidValue.isEmpty()) {
            message.append(String.format(" | Invalid Value: %s", invalidValue));
        }

        if (additionalMessage != null && !additionalMessage.isEmpty()) {
            message.append(String.format(" | Details: %s", additionalMessage));
        }

        return message.toString();
    }


    private void logException() {
        logger.warn("Invalid Exception occurred: {} | Field: {} | Invalid Value: {} | Context: {} | Timestamp: {}",
                getMessage(), fieldName, invalidValue, requestContext, timestamp);

        if (logger.isDebugEnabled()) {
            logger.debug("Stack trace for InvalidException", this);
        }
    }

    public ErrorCode getErrorCode() { return errorCode; }
    public String getFieldName() { return fieldName; }
    public String getInvalidValue() { return invalidValue; }
    public String getTimestamp() { return timestamp; }
    public String getRequestContext() { return requestContext; }


    public ApiErrorResponse toApiErrorResponse() {
        return new ApiErrorResponse(
                errorCode.getCode(),
                getMessage(),
                fieldName,
                invalidValue,
                timestamp,
                requestContext
        );
    }


    public static class ApiErrorResponse {
        private final String errorCode;
        private final String message;
        private final String fieldName;
        private final String invalidValue;
        private final String timestamp;
        private final String context;

        public ApiErrorResponse(String errorCode, String message, String fieldName,
                                String invalidValue, String timestamp, String context) {
            this.errorCode = errorCode;
            this.message = message;
            this.fieldName = fieldName;
            this.invalidValue = invalidValue;
            this.timestamp = timestamp;
            this.context = context;
        }

        public String getErrorCode() { return errorCode; }
        public String getMessage() { return message; }
        public String getFieldName() { return fieldName; }
        public String getInvalidValue() { return invalidValue; }
        public String getTimestamp() { return timestamp; }
        public String getContext() { return context; }
    }


    public static class Builder {
        public static InvalidException invalidField(String fieldName, String invalidValue) {
            return new InvalidException(ErrorCode.INVALID_INPUT, fieldName, invalidValue);
        }

        public static InvalidException invalidDateFormat(String fieldName, String invalidValue) {
            return new InvalidException(ErrorCode.INVALID_DATE_FORMAT, fieldName, invalidValue);
        }

        public static InvalidException missingField(String fieldName) {
            return new InvalidException(ErrorCode.MISSING_REQUIRED_FIELD, fieldName, "null");
        }

        public static InvalidException invalidId(String fieldName, String invalidValue) {
            return new InvalidException(ErrorCode.INVALID_ID, fieldName, invalidValue);
        }
    }
    
}
