package com.hospital.api.exception.auth;

public class InvalidTokenException extends AuthException {
    
    public InvalidTokenException(String username, String resource, String additionalMessage, String context) {
        super(ErrorCode.INVALID_TOKEN, username, resource, additionalMessage, context);
    }

    public InvalidTokenException(String username, String resource) {
        super(ErrorCode.INVALID_TOKEN, username, resource);
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}