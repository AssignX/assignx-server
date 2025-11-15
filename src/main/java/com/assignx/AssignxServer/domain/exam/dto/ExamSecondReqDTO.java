package com.assignx.AssignxServer.domain.exam.dto;

import com.assignx.AssignxServer.domain.exam.entity.ExamType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ExamSecondReqDTO(
        @NotNull
        Long examId,
        @NotNull
        ExamType examType,
        @NotNull
        LocalDateTime startTime,
        @NotNull
        LocalDateTime endTime,
        @NotNull
        Long examRoomId
) {

}
