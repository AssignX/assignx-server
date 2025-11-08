package com.assignx.AssignxServer.global.auth.dto;

import com.assignx.AssignxServer.domain.member.entity.Member;
import com.assignx.AssignxServer.domain.member.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LoginResDTO(
        @NotNull
        Long memberId,
        @NotBlank
        String name,
        @NotNull
        Role role,
        @NotBlank
        String idNumber,
        @NotNull
        Long departmentId,
        @NotBlank
        String departmentName,
        @NotBlank
        String accessToken,
        @NotBlank
        String refreshToken
) {
    public static LoginResDTO fromEntity(Member member, String accessToken, String refreshToken) {
        return LoginResDTO.builder()
                .memberId(member.getId())
                .name(member.getName())
                .role(member.getRole())
                .idNumber(member.getIdNumber())
                .departmentId(member.getDepartment().getId())
                .departmentName(member.getDepartment().getMajor())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
