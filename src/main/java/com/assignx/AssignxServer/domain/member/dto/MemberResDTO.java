package com.assignx.AssignxServer.domain.member.dto;

import com.assignx.AssignxServer.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MemberResDTO(
        @NotNull
        Long memberId,
        @NotBlank
        String name,
        @NotBlank
        String idNumber,
        @NotBlank
        String departmentName
) {
    public static MemberResDTO fromEntity(Member member) {
        return MemberResDTO.builder()
                .memberId(member.getId())
                .name(member.getName())
                .idNumber(member.getIdNumber())
                .departmentName(member.getDepartment().getMajor())
                .build();
    }

}
