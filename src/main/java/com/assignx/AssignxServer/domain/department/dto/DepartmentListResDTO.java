package com.assignx.AssignxServer.domain.department.dto;

import com.assignx.AssignxServer.domain.department.entity.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DepartmentListResDTO(
        @NotNull
        Long departmentId,
        @NotBlank
        String college,
        @NotBlank
        String major
) {
    public static DepartmentListResDTO fromEntity(Department department) {
        return DepartmentListResDTO.builder()
                .departmentId(department.getId())
                .college(department.getCollege())
                .major(department.getMajor())
                .build();
    }
}
