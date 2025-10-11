package com.assignx.AssignxServer.domain.course.dto;

import com.assignx.AssignxServer.domain.course.entity.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CourseResDTO(
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

        String professorName,
        Boolean isProfessorMapped,

        String buildingName,
        String roomNumber,

        String college,
        String major
) {
    public static CourseResDTO fromEntity(Course course) {
        int professorCnt = course.getProfessorName().split(",").length;

        // TODO 실제로는 이름도 같은지 체크해야하지만 우선은 간단하게 size만 비교
        Boolean isProfMapped = professorCnt == course.getProfessors().size();

        return CourseResDTO.builder()
                .courseId(course.getId())
                .courseName(course.getCourseName())
                .courseCode(course.getCourseCode())
                .courseTime(course.getCourseTime())
                .enrolledCount(course.getEnrolledCount())
                .year(course.getYear())
                .semester(course.getSemester())
                .professorName(course.getProfessorName())
                .isProfessorMapped(isProfMapped)
                .buildingName(course.getRoom().getBuilding().getBuildingName())
                .roomNumber(course.getRoom().getRoomNumber())
                .college(course.getDepartment().getCollege())
                .major(course.getDepartment().getMajor())
                .build();
    }
}
