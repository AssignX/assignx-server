package com.assignx.AssignxServer.domain.exam.exception;

import org.springframework.http.HttpStatus;

public class ExamExceptionUtils {

    public static ExamException ExamIdsEmpty() {
        return new ExamException(HttpStatus.BAD_REQUEST, "시험 ID 목록이 비어있습니다.");
    }

    public static ExamException ExamNotFound() {
        return new ExamException(HttpStatus.NOT_FOUND, "시험을 찾을 수 없습니다.");
    }

    public static ExamException ListSizeUnmatched() {
        return new ExamException(HttpStatus.BAD_REQUEST, "시험 ID, 시험 시간, 시험 장소 List의 size가 일치하지 않습니다.");
    }
}
