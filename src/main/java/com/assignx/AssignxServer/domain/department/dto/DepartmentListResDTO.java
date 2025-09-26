package com.assignx.AssignxServer.domain.department.dto;

import com.assignx.AssignxServer.domain.department.entity.Department;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DepartmentListResDTO(
        @NotBlank
        String college,
        @NotBlank
        String major
) {
    public static DepartmentListResDTO fromEntity(Department department) {
        return DepartmentListResDTO.builder()
                .college(department.getCollege())
                .major(department.getMajor())
                .build();
    }
}
