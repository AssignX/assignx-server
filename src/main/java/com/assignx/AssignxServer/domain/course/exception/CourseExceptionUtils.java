package com.assignx.AssignxServer.domain.course.exception;

import org.springframework.http.HttpStatus;

public class CourseExceptionUtils {

    public static CourseException SemesterParsingFailed() {
        return new CourseException(HttpStatus.INTERNAL_SERVER_ERROR, "SY에서 불러온 과목에서 학기 정보를 찾을 수 없습니다.");
    }

    public static CourseException UnknownSemester(String input) {
        String message = "알 수 없는 학기 정보입니다. (입력값: " + input + ")";
        return new CourseException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static CourseException CourseAlreadyExists() {
        throw new CourseException(HttpStatus.CONFLICT, "이미 존재하는 강의입니다.");
    }

    public static CourseException CourseNotFound() {
        throw new CourseException(HttpStatus.NOT_FOUND, "강의가 존재하지 않습니다.");
    }

}
