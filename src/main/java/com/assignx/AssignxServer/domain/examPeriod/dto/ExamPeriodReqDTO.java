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
        LocalDateTime midStartDateTime,
        @NotNull
        LocalDateTime midEndDateTime,
        @NotNull
        LocalDateTime finalStartDateTime,
        @NotNull
        LocalDateTime finalEndDateTime
) {
    public ExamPeriod toEntity() {
        return ExamPeriod.builder()
                .year(year)
                .semester(semester)
                .midStartDateTime(midStartDateTime)
                .midEndDateTime(midEndDateTime)
                .finalStartDateTime(finalStartDateTime)
                .finalEndDateTime(finalEndDateTime)
                .build();
    }

}
