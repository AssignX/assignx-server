package com.assignx.AssignxServer.domain.courseProfessor.repository;

import com.assignx.AssignxServer.domain.courseProfessor.entity.CourseProfessor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseProfessorRepository extends JpaRepository<CourseProfessor, Long> {
}
