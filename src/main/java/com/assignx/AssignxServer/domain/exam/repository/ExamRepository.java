package com.assignx.AssignxServer.domain.exam.repository;

import com.assignx.AssignxServer.domain.exam.entity.Exam;
import com.assignx.AssignxServer.domain.exam.entity.ExamAssigned;
import com.assignx.AssignxServer.domain.exam.entity.ExamType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ExamRepository extends JpaRepository<Exam, Long>, JpaSpecificationExecutor {
    @Query("""
                SELECT e FROM Exam e 
                WHERE e.startTime < :endTime
                  AND e.endTime > :startTime
            """)
    List<Exam> findAllOverlapping(LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT e FROM Exam e " +
            "WHERE e.course.year = :year " +
            "AND e.course.semester = :semester " +
            "AND e.examType = :examType " +
            "AND e.examAssigned = :examAssigned")
    List<Exam> findAllByYearSemesterAndExamTypeAndAssigned(
            String year, String semester, ExamType examType, ExamAssigned examAssigned
    );
}
