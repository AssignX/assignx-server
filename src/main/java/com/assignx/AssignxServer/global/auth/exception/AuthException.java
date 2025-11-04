package com.assignx.AssignxServer.global.auth.exception;

import com.jhssong.errorping.exception.BaseDomainException;
import org.springframework.http.HttpStatus;

public class AuthException extends BaseDomainException {
    protected AuthException(HttpStatus status, String message) {
        super(status, message);
    }

}
