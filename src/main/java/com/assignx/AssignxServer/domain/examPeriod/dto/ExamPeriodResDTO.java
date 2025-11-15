package com.assignx.AssignxServer.domain.examPeriod.dto;

import com.assignx.AssignxServer.domain.examPeriod.entity.ExamPeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ExamPeriodResDTO(
        @NotNull
        Long examPeriodId,
        @NotBlank
        String year,
        @NotBlank
        String semester,
        @NotNull
        LocalDateTime midStartDateTime,
        @NotNull
        LocalDateTime midEndDateTime,
        @NotNull
        LocalDateTime finalStartDateTime,
        @NotNull
        LocalDateTime finalEndDateTime
) {
    public static ExamPeriodResDTO fromEntity(ExamPeriod entity) {
        return ExamPeriodResDTO.builder()
                .examPeriodId(entity.getId())
                .year(entity.getYear())
                .semester(entity.getSemester())
                .midStartDateTime(entity.getMidStartDateTime())
                .midEndDateTime(entity.getMidEndDateTime())
                .finalStartDateTime(entity.getFinalStartDateTime())
                .finalEndDateTime(entity.getFinalEndDateTime())
                .build();
    }

}
