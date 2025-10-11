package com.assignx.AssignxServer.domain.course.repository;

import com.assignx.AssignxServer.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
