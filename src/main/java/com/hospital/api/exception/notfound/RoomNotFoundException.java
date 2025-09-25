package com.hospital.api.exception.notfound;

public class RoomNotFoundException extends ResourceNotFoundException {

    public RoomNotFoundException(String roomId, String additionalMessage, String context) {
        super(ErrorCode.RESOURCE_NOT_FOUND, "Room", roomId, additionalMessage, context);
    }

    public RoomNotFoundException(String roomId) {
        super(ErrorCode.RESOURCE_NOT_FOUND, "Room", roomId);
    }

    public RoomNotFoundException(String exceptionMessage, boolean legacy) {
        super(exceptionMessage);
    }

}
