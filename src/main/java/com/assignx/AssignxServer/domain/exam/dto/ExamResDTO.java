package com.assignx.AssignxServer.domain.exam.dto;

import com.assignx.AssignxServer.domain.exam.entity.Exam;
import com.assignx.AssignxServer.domain.exam.entity.ExamAssigned;
import com.assignx.AssignxServer.domain.exam.entity.ExamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ExamResDTO(
        @NotNull
        Long examId,
        @NotNull
        Long courseId,
        @NotBlank
        String courseName,
        @NotBlank
        String courseCode,
        @NotBlank
        String courseTime,
        @NotNull
        int enrolledCount,
        @NotBlank
        String year,
        @NotBlank
        String semester,
        @NotBlank
        String buildingName,
        @NotBlank
        String roomNumber,
        @NotBlank
        LocalDateTime startTime,
        @NotBlank
        LocalDateTime endTime,
        @NotNull
        ExamType examType,
        @NotNull
        ExamAssigned examAssigned
) {
    public static ExamResDTO fromEntity(Exam exam) {
        return ExamResDTO.builder()
                .examId(exam.getId())
                .courseId(exam.getCourse().getId())
                .courseName(exam.getCourse().getCourseName())
                .courseCode(exam.getCourse().getCourseCode())
                .courseTime(exam.getCourse().getCourseTime())
                .enrolledCount(exam.getCourse().getEnrolledCount())
                .year(exam.getCourse().getYear())
                .semester(exam.getCourse().getSemester())
                .buildingName(exam.getExamRoom().getBuilding().getBuildingName())
                .roomNumber(exam.getExamRoom().getRoomNumber())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .examType(exam.getExamType())
                .examAssigned(exam.getExamAssigned())
                .build();
    }

}
