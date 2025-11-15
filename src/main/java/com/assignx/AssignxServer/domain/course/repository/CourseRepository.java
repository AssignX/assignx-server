package com.assignx.AssignxServer.domain.course.repository;

import com.assignx.AssignxServer.domain.course.entity.Course;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor {
    Optional<Course> findByCourseCodeAndSemester(String courseCode, String semester);

    List<Course> findAllByYearAndSemester(String year, String semester);
}
