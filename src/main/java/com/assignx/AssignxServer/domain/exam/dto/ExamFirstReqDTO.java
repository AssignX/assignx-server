package com.assignx.AssignxServer.domain.exam.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ExamFirstReqDTO(
        @NotNull
        List<Long> examIds,
        boolean isApply
) {
}
