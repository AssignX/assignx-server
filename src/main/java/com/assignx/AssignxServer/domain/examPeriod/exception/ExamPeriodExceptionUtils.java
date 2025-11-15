package com.assignx.AssignxServer.domain.examPeriod.exception;

import org.springframework.http.HttpStatus;

public class ExamPeriodExceptionUtils {

    public static ExamPeriodException ExamPeriodNotFound() {
        return new ExamPeriodException(HttpStatus.NOT_FOUND, "등록된 시험 신청 기간이 없습니다.");
    }

    public static ExamPeriodException NotInSemester() {
        return new ExamPeriodException(HttpStatus.BAD_REQUEST, "학기 중이 아닙니다.");
    }

    public static ExamPeriodException NotInApplyPeriod() {
        return new ExamPeriodException(HttpStatus.BAD_REQUEST, "시험 신청 기간이 아닙니다.");
    }

    public static ExamPeriodException UnknownExamPeriod(String period) {
        String message = "Period 값이 잘못되었습니다." + period;
        return new ExamPeriodException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
