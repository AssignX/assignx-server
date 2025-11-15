package com.assignx.AssignxServer.domain.exam.dto;

import com.assignx.AssignxServer.domain.course.entity.Course;
import com.assignx.AssignxServer.domain.exam.entity.Exam;
import com.assignx.AssignxServer.domain.exam.entity.ExamAssigned;
import com.assignx.AssignxServer.domain.exam.entity.ExamType;
import com.assignx.AssignxServer.domain.room.entity.Room;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ExamResDTO(
        @NotNull
        Long examId,
        @NotNull
        Course course,
        @NotBlank
        String examTime,
        @NotNull
        ExamType examType,
        @NotNull
        ExamAssigned examAssigned,
        @NotNull
        Room examRoom
) {
    public static ExamResDTO fromEntity(Exam exam) {
        return ExamResDTO.builder()
                .examId(exam.getId())
                .course(exam.getCourse())
                .examTime(exam.getExamTime())
                .examType(exam.getExamType())
                .examAssigned(exam.getExamAssigned())
                .examRoom(exam.getExamRoom())
                .build();
    }

}
