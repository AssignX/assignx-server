package com.assignx.AssignxServer.domain.room.exception;

import org.springframework.http.HttpStatus;

public class RoomExceptionUtils {

    public static RoomException RoomAlreadyExists() {
        return new RoomException(HttpStatus.CONFLICT, "이미 존재하는 강의실입니다.");
    }

    public static RoomException RoomNotExist() {
        return new RoomException(HttpStatus.NOT_FOUND, "건물이 존재하지 않습니다.");
    }
}
