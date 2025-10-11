package com.assignx.AssignxServer.domain.course.dto;

import com.assignx.AssignxServer.domain.course.entity.Course;
import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.member.entity.Member;
import com.assignx.AssignxServer.domain.room.entity.Room;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record CourseCreateReqDTO(
        @NotBlank
        String courseName,
        @NotBlank
        String courseCode,
        @NotBlank
        String courseTime,
        @NotNull
        int enrolledCount,
        @NotBlank
        String semester,
        String professorName,
        List<Member> professors,
        @NotNull
        Room room,
        @NotNull
        Department department
) {
    public Course toEntity() {
        return Course.builder()
                .courseCode(courseCode)
                .courseName(courseName)
                .courseTime(courseTime)
                .enrolledCount(enrolledCount)
                .semester(semester)
                .professorName(professorName)
                .room(room)
                .department(department)
                .build();
    }

}