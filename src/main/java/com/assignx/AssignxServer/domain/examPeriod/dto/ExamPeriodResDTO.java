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
        LocalDateTime midFirstStartDateTime,
        @NotNull
        LocalDateTime midFirstEndDateTime,
        @NotNull
        LocalDateTime midSecondStartDateTime,
        @NotNull
        LocalDateTime midSecondEndDateTime,
        @NotNull
        LocalDateTime finalFirstStartDateTime,
        @NotNull
        LocalDateTime finalFirstEndDateTime,
        @NotNull
        LocalDateTime finalSecondStartDateTime,
        @NotNull
        LocalDateTime finalSecondEndDateTime
) {
    public static ExamPeriodResDTO fromEntity(ExamPeriod entity) {
        return ExamPeriodResDTO.builder()
                .examPeriodId(entity.getId())
                .year(entity.getYear())
                .semester(entity.getSemester())
                .midFirstStartDateTime(entity.getMidFirstStartDateTime())
                .midFirstEndDateTime(entity.getMidFirstEndDateTime())
                .midSecondStartDateTime(entity.getMidSecondStartDateTime())
                .midSecondEndDateTime(entity.getMidSecondEndDateTime())
                .finalFirstStartDateTime(entity.getFinalFirstStartDateTime())
                .finalFirstEndDateTime(entity.getFinalFirstEndDateTime())
                .finalSecondStartDateTime(entity.getFinalSecondStartDateTime())
                .finalSecondEndDateTime(entity.getFinalSecondEndDateTime())
                .build();
    }

}
