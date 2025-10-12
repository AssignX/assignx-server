package com.assignx.AssignxServer.domain.member.exception;

import org.springframework.http.HttpStatus;

public class MemberExceptionUtils {

    public static MemberException ProfessorNotFound() {
        return new MemberException(HttpStatus.NOT_FOUND, "교수가 존재하지 않습니다.");
    }
}
