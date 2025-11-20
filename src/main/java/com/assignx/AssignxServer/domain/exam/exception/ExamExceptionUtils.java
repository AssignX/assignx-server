package com.assignx.AssignxServer.domain.exam.exception;

import org.springframework.http.HttpStatus;

public class ExamExceptionUtils {

    public static ExamException ExamNotFound() {
        return new ExamException(HttpStatus.NOT_FOUND, "시험을 찾을 수 없습니다.");
    }

    public static ExamException ExamScheduleConflict() {
        return new ExamException(HttpStatus.BAD_REQUEST, "기존 시험 일정과 충돌합니다.");
    }

}
