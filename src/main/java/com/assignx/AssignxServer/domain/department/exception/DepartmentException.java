package com.assignx.AssignxServer.domain.department.exception;

import com.jhssong.errorping.exception.BaseDomainException;
import org.springframework.http.HttpStatus;

public class DepartmentException extends BaseDomainException {

    protected DepartmentException(HttpStatus status, String message) {
        super(status, message);
    }
}
