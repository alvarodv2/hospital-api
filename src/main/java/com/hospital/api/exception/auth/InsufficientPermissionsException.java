package com.hospital.api.exception.auth;

public class InsufficientPermissionsException extends AuthException {
    
    public InsufficientPermissionsException(String username, String resource, String additionalMessage, String context) {
        super(ErrorCode.INSUFFICIENT_PERMISSIONS, username, resource, additionalMessage, context);
    }

    public InsufficientPermissionsException(String username, String resource) {
        super(ErrorCode.INSUFFICIENT_PERMISSIONS, username, resource);
    }

    public InsufficientPermissionsException(String message) {
        super(message);
    }
}