package com.assignx.AssignxServer.domain.course.repository;

import com.assignx.AssignxServer.domain.course.entity.Course;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCodeAndSemester(String courseCode, String semester);

    List<Course> findByYearAndSemesterAndRoom_RoomNumber(String year, String semester, String roomNumber);

    @Query("SELECT cp.course FROM CourseProfessor cp " +
            "WHERE cp.course.department.major = :major AND cp.professor.name LIKE %:professorName%")
    List<Course> findCoursesByMajorAndProfessorName(String major, String professorName);

    @Query("SELECT cp.course FROM CourseProfessor cp " +
            "WHERE cp.course.department.major = :major AND cp.professor.id = :professorId")
    List<Course> findCoursesByMajorAndProfessorId(String major, Long professorId);

}
