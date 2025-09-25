package com.hospital.api.exception.conflict;

public class SchedulingConflictException extends ConflictException {
    
    public SchedulingConflictException(String resourceType, String conflictingValue, String additionalMessage, String context) {
        super(ErrorCode.SCHEDULING_CONFLICT, resourceType, conflictingValue, additionalMessage, context);
    }

    public SchedulingConflictException(String resourceType, String conflictingValue) {
        super(ErrorCode.SCHEDULING_CONFLICT, resourceType, conflictingValue);
    }

    public SchedulingConflictException(String message) {
        super(message);
    }
}