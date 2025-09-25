package com.hospital.api.exception.invalid;

public class InvalidRoomException extends InvalidException {

    public InvalidRoomException(String invalidValue, String additionalMessage, String context) {
        super(ErrorCode.INVALID_ROOM, "Room", invalidValue, additionalMessage, context);
    }

    public InvalidRoomException(String invalidValue) {
        super(ErrorCode.INVALID_ROOM, "Room", invalidValue);
    }

    public InvalidRoomException(String exceptionMessage, boolean legacy) {
        super(exceptionMessage);
    }
}
