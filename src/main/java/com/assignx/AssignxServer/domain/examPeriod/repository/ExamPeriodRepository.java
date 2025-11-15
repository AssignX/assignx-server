package com.assignx.AssignxServer.domain.examPeriod.repository;

import com.assignx.AssignxServer.domain.examPeriod.entity.ExamPeriod;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamPeriodRepository extends JpaRepository<ExamPeriod, Long> {
    Optional<ExamPeriod> findByYearAndSemester(String year, String semester);
}
