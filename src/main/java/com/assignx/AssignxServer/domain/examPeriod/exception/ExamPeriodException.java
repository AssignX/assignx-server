package com.assignx.AssignxServer.domain.examPeriod.exception;

import com.jhssong.errorping.exception.BaseDomainException;
import org.springframework.http.HttpStatus;

public class ExamPeriodException extends BaseDomainException {

    protected ExamPeriodException(HttpStatus status, String message) {
        super(status, message);
    }
}
