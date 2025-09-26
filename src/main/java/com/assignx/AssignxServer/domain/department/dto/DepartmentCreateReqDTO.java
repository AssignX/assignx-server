package com.assignx.AssignxServer.domain.department.dto;

import com.assignx.AssignxServer.domain.department.entity.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record DepartmentCreateReqDTO(
        @NotBlank
        String college,
        @NotBlank
        String major,
        @NotNull
        List<Long> employeeIds,
        @NotNull
        List<Long> roomIds
) {
    public Department toEntity() {
        return Department.builder()
                .college(college)
                .major(major)
                .build();
    }
}
