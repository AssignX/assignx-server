package com.assignx.AssignxServer.domain.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record DepartmentReqDTO(
        @NotNull
        Long departmentId,
        @NotBlank
        String college,
        @NotBlank
        String major,
        @NotNull
        List<Long> employeeIds,
        @NotNull
        List<Long> roomIds
) {
}
