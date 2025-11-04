package com.assignx.AssignxServer.global.auth.exception;

import org.springframework.http.HttpStatus;

public class AuthExceptionUtils {

    public static AuthException PasswordNotMatch() {
        return new AuthException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
    }

    public static AuthException RefreshTokenNotFound() {
        return new AuthException(HttpStatus.UNAUTHORIZED, "로그아웃 상태입니다.");
    }

    public static AuthException InvalidRefreshToken() {
        return new AuthException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.");
    }


}
