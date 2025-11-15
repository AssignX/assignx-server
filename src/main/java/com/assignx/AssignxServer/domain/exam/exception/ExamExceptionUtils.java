package com.assignx.AssignxServer.domain.exam.exception;

import org.springframework.http.HttpStatus;

public class ExamExceptionUtils {

    public static ExamException FirstApplyEmpty() {
        return new ExamException(HttpStatus.BAD_REQUEST, "시험 ID 목록이 비어있습니다.");
    }
}
