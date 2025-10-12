package com.assignx.AssignxServer.domain.department.exception;

import org.springframework.http.HttpStatus;

public class DepartmentExceptionUtils {

    public static DepartmentException DepartmentAlreadyExists() {
        return new DepartmentException(HttpStatus.CONFLICT, "이미 존재하는 학과입니다.");
    }

    public static DepartmentException DepartmentNotExist() {
        return new DepartmentException(HttpStatus.NOT_FOUND, "학과가 존재하지 않습니다.");
    }

    public static DepartmentException DepartmentParsingFailed(String departmentName) {
        String message = "학과를 특정할 수 없습니다. (검색어: " + departmentName + ")";
        return new DepartmentException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
