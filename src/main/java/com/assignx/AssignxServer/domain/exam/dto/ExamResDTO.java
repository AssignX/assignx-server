package com.assignx.AssignxServer.domain.exam.dto;

import com.assignx.AssignxServer.domain.exam.entity.Exam;
import com.assignx.AssignxServer.domain.exam.entity.ExamAssigned;
import com.assignx.AssignxServer.domain.exam.entity.ExamType;
import com.assignx.AssignxServer.domain.room.entity.Room;
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
        Long roomId,
        String buildingName,
        String roomNumber,
        LocalDateTime startTime,
        LocalDateTime endTime,
        @NotNull
        ExamType examType,
        @NotNull
        ExamAssigned examAssigned
) {
    public static ExamResDTO fromEntity(Exam exam) {

        Room room = exam.getExamRoom();
        String buildingName = null, roomNumber = null;
        Long roomId = null;
        if (room != null) {
            buildingName = room.getBuilding().getBuildingName();
            roomNumber = room.getRoomNumber();
            roomId = room.getId();
        }

        return ExamResDTO.builder()
                .examId(exam.getId())
                .courseId(exam.getCourse().getId())
                .courseName(exam.getCourse().getCourseName())
                .courseCode(exam.getCourse().getCourseCode())
                .courseTime(exam.getCourse().getCourseTime())
                .enrolledCount(exam.getCourse().getEnrolledCount())
                .year(exam.getCourse().getYear())
                .semester(exam.getCourse().getSemester())
                .roomId(roomId)
                .buildingName(buildingName)
                .roomNumber(roomNumber)
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .examType(exam.getExamType())
                .examAssigned(exam.getExamAssigned())
                .build();
    }

}
