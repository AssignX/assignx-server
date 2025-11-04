package com.assignx.AssignxServer.global.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginReqDTO(
        @NotBlank
        String idNumber,
        @NotBlank
        String password
) {
}
