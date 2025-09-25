package com.hospital.api.exception.auth;

public class UnauthorizedException extends AuthException {
    
    public UnauthorizedException(String username, String resource, String additionalMessage, String context) {
        super(ErrorCode.UNAUTHORIZED, username, resource, additionalMessage, context);
    }

    public UnauthorizedException(String username, String resource) {
        super(ErrorCode.UNAUTHORIZED, username, resource);
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}