package com.hospital.api.exception.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base exception for authentication and authorization errors in the Hospital API.
 */
public class AuthException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(AuthException.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum ErrorCode {
        UNAUTHORIZED("AUTH_401", "Unauthorized access"),
        INVALID_TOKEN("AUTH_401", "Invalid or expired token"),
        INVALID_CREDENTIALS("AUTH_401", "Invalid username or password"),
        INSUFFICIENT_PERMISSIONS("AUTH_403", "Insufficient permissions"),
        TOKEN_EXPIRED("AUTH_401", "Token has expired");

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
    private final String username;
    private final String resource;
    private final String timestamp;
    private final String requestContext;

    public AuthException(ErrorCode errorCode, String username,
                        String resource, String additionalMessage,
                        String requestContext) {
        super(buildDetailedMessage(errorCode, username, resource, additionalMessage));
        this.errorCode = errorCode;
        this.username = username;
        this.resource = resource;
        this.requestContext = requestContext;
        this.timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);

        logException();
    }

    public AuthException(ErrorCode errorCode, String username, String resource) {
        this(errorCode, username, resource, null, "API Request");
    }

    public AuthException(String message) {
        this(ErrorCode.UNAUTHORIZED, "Unknown", "Unknown", message, "Legacy Call");
    }

    private static String buildDetailedMessage(ErrorCode errorCode, String username,
                                             String resource, String additionalMessage) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("[%s] %s", errorCode.getCode(), errorCode.getDescription()));

        if (username != null && !username.isEmpty()) {
            message.append(String.format(" | User: %s", username));
        }

        if (resource != null && !resource.isEmpty()) {
            message.append(String.format(" | Resource: %s", resource));
        }

        if (additionalMessage != null && !additionalMessage.isEmpty()) {
            message.append(String.format(" | Details: %s", additionalMessage));
        }

        return message.toString();
    }

    private void logException() {
        logger.warn("Auth Exception occurred: {} | User: {} | Resource: {} | Context: {} | Timestamp: {}",
                getMessage(), username, resource, requestContext, timestamp);

        if (logger.isDebugEnabled()) {
            logger.debug("Stack trace for AuthException", this);
        }
    }

    public ErrorCode getErrorCode() { return errorCode; }
    public String getUsername() { return username; }
    public String getResource() { return resource; }
    public String getTimestamp() { return timestamp; }
    public String getRequestContext() { return requestContext; }

    public ApiErrorResponse toApiErrorResponse() {
        return new ApiErrorResponse(
                errorCode.getCode(),
                getMessage(),
                username,
                resource,
                timestamp,
                requestContext
        );
    }

    public static class ApiErrorResponse {
        private final String errorCode;
        private final String message;
        private final String username;
        private final String resource;
        private final String timestamp;
        private final String context;

        public ApiErrorResponse(String errorCode, String message, String username,
                              String resource, String timestamp, String context) {
            this.errorCode = errorCode;
            this.message = message;
            this.username = username;
            this.resource = resource;
            this.timestamp = timestamp;
            this.context = context;
        }

        public String getErrorCode() { return errorCode; }
        public String getMessage() { return message; }
        public String getUsername() { return username; }
        public String getResource() { return resource; }
        public String getTimestamp() { return timestamp; }
        public String getContext() { return context; }
    }
}