package com.assignx.AssignxServer.domain.examPeriod.dto;

import com.assignx.AssignxServer.domain.examPeriod.entity.ExamPeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ExamPeriodReqDTO(
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
    public ExamPeriod toEntity() {
        return ExamPeriod.builder()
                .year(year)
                .semester(semester)
                .midFirstStartDateTime(midFirstStartDateTime)
                .midFirstEndDateTime(midFirstEndDateTime)
                .midSecondStartDateTime(midSecondStartDateTime)
                .midSecondEndDateTime(midSecondEndDateTime)
                .finalFirstStartDateTime(finalFirstStartDateTime)
                .finalFirstEndDateTime(finalFirstEndDateTime)
                .finalSecondStartDateTime(finalSecondStartDateTime)
                .finalSecondEndDateTime(finalSecondEndDateTime)
                .build();
    }

}
