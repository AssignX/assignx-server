package com.assignx.AssignxServer.global.auth.dto;

public record TokenReissueReqDTO(
        String accessToken,
        String refreshToken
) {
}
