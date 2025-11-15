package com.assignx.AssignxServer.domain.exam.dto;

import com.assignx.AssignxServer.domain.exam.entity.ExamType;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ExamFirstReqDTO(
        @NotNull
        List<Long> examIds,
        @NotNull
        ExamType examType,
        @NotNull
        boolean isApply
) {
}
