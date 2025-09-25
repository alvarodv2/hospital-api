package com.hospital.api.exception.conflict;

public class DuplicateResourceException extends ConflictException {
    
    public DuplicateResourceException(String resourceType, String conflictingValue, String additionalMessage, String context) {
        super(ErrorCode.DUPLICATE_RESOURCE, resourceType, conflictingValue, additionalMessage, context);
    }

    public DuplicateResourceException(String resourceType, String conflictingValue) {
        super(ErrorCode.DUPLICATE_RESOURCE, resourceType, conflictingValue);
    }

    public DuplicateResourceException(String message) {
        super(message);
    }
}