package com.assignx.AssignxServer.domain.member.exception;

import com.jhssong.errorping.exception.BaseDomainException;
import org.springframework.http.HttpStatus;

public class MemberException extends BaseDomainException {

    protected MemberException(HttpStatus status, String message) {
        super(status, message);
    }
}
