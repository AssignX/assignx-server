package com.assignx.AssignxServer.global.auth.dto;

public record TokenReissueResDTO(
        String accessToken,
        String refreshToken
) {
}
