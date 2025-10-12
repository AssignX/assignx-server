package com.assignx.AssignxServer.domain.course.exception;

import com.jhssong.errorping.exception.BaseDomainException;
import org.springframework.http.HttpStatus;

public class CourseException extends BaseDomainException {

    protected CourseException(HttpStatus status, String message) {
        super(status, message);
    }
}
