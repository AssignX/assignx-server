package com.assignx.AssignxServer.domain.exam.exception;

import com.jhssong.errorping.exception.BaseDomainException;
import org.springframework.http.HttpStatus;

public class ExamException extends BaseDomainException {

    protected ExamException(HttpStatus status, String message) {
        super(status, message);
    }
}
