package com.hospital.api.exception.conflict;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base exception for handling resource conflicts in the Hospital API.
 */
public class ConflictException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ConflictException.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum ErrorCode {
        DUPLICATE_RESOURCE("CONFLICT_409", "Resource already exists"),
        SCHEDULING_CONFLICT("CONFLICT_409", "Time slot already booked"),
        RESOURCE_BUSY("CONFLICT_409", "Resource is currently in use"),
        VERSION_CONFLICT("CONFLICT_409", "Resource version mismatch"),
        DEPENDENCY_CONFLICT("CONFLICT_409", "Resource has dependencies");

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
    private final String conflictingValue;
    private final String timestamp;
    private final String requestContext;

    public ConflictException(ErrorCode errorCode, String resourceType,
                           String conflictingValue, String additionalMessage,
                           String requestContext) {
        super(buildDetailedMessage(errorCode, resourceType, conflictingValue, additionalMessage));
        this.errorCode = errorCode;
        this.resourceType = resourceType;
        this.conflictingValue = conflictingValue;
        this.requestContext = requestContext;
        this.timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);

        logException();
    }

    public ConflictException(ErrorCode errorCode, String resourceType, String conflictingValue) {
        this(errorCode, resourceType, conflictingValue, null, "API Request");
    }

    public ConflictException(String message) {
        this(ErrorCode.DUPLICATE_RESOURCE, "Unknown", "Unknown", message, "Legacy Call");
    }

    private static String buildDetailedMessage(ErrorCode errorCode, String resourceType,
                                             String conflictingValue, String additionalMessage) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("[%s] %s", errorCode.getCode(), errorCode.getDescription()));

        if (resourceType != null && !resourceType.isEmpty()) {
            message.append(String.format(" | Resource Type: %s", resourceType));
        }

        if (conflictingValue != null && !conflictingValue.isEmpty()) {
            message.append(String.format(" | Conflicting Value: %s", conflictingValue));
        }

        if (additionalMessage != null && !additionalMessage.isEmpty()) {
            message.append(String.format(" | Details: %s", additionalMessage));
        }

        return message.toString();
    }

    private void logException() {
        logger.warn("Conflict Exception occurred: {} | Resource Type: {} | Conflicting Value: {} | Context: {} | Timestamp: {}",
                getMessage(), resourceType, conflictingValue, requestContext, timestamp);

        if (logger.isDebugEnabled()) {
            logger.debug("Stack trace for ConflictException", this);
        }
    }

    public ErrorCode getErrorCode() { return errorCode; }
    public String getResourceType() { return resourceType; }
    public String getConflictingValue() { return conflictingValue; }
    public String getTimestamp() { return timestamp; }
    public String getRequestContext() { return requestContext; }

    public ApiErrorResponse toApiErrorResponse() {
        return new ApiErrorResponse(
                errorCode.getCode(),
                getMessage(),
                resourceType,
                conflictingValue,
                timestamp,
                requestContext
        );
    }

    public static class ApiErrorResponse {
        private final String errorCode;
        private final String message;
        private final String resourceType;
        private final String conflictingValue;
        private final String timestamp;
        private final String context;

        public ApiErrorResponse(String errorCode, String message, String resourceType,
                              String conflictingValue, String timestamp, String context) {
            this.errorCode = errorCode;
            this.message = message;
            this.resourceType = resourceType;
            this.conflictingValue = conflictingValue;
            this.timestamp = timestamp;
            this.context = context;
        }

        public String getErrorCode() { return errorCode; }
        public String getMessage() { return message; }
        public String getResourceType() { return resourceType; }
        public String getConflictingValue() { return conflictingValue; }
        public String getTimestamp() { return timestamp; }
        public String getContext() { return context; }
    }
}