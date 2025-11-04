package com.assignx.AssignxServer.global.auth.dto;

public record TokenResDTO(
        String accessToken,
        String refreshToken
) {
}
