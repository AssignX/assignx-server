package com.assignx.AssignxServer.domain.course.repository;

import com.assignx.AssignxServer.domain.course.entity.Course;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCodeAndSemester(String courseCode, String semester);
}
