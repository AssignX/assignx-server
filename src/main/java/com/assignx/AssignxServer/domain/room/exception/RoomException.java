package com.assignx.AssignxServer.domain.room.exception;

import com.jhssong.errorping.exception.BaseDomainException;
import org.springframework.http.HttpStatus;

public class RoomException extends BaseDomainException {

    protected RoomException(HttpStatus status, String message) {
        super(status, message);
    }
}
