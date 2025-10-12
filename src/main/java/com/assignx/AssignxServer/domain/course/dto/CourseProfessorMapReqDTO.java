package com.assignx.AssignxServer.domain.course.dto;

import jakarta.validation.constraints.NotNull;

public record CourseProfessorMapReqDTO(
        @NotNull
        Long courseId,
        @NotNull
        Long professorId
) {
}
