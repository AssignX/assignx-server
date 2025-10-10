package com.assignx.AssignxServer.domain.room.exception;

import java.util.Set;
import org.springframework.http.HttpStatus;

public class RoomExceptionUtils {

    public static RoomException RoomAlreadyExists() {
        return new RoomException(HttpStatus.CONFLICT, "이미 존재하는 강의실입니다.");
    }

    public static RoomException RoomAlreadyExists(Set<String> existingRoomNumbers) {
        String message = "이미 존재하는 강의실입니다.";

        if (existingRoomNumbers != null && !existingRoomNumbers.isEmpty()) {
            String numbers = String.join(", ", existingRoomNumbers);
            message += " (번호: " + numbers + ")";
        }
        return new RoomException(HttpStatus.CONFLICT, message);
    }

    public static RoomException RoomNotExist() {
        return new RoomException(HttpStatus.NOT_FOUND, "건물이 존재하지 않습니다.");
    }
}
