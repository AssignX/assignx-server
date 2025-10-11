package com.assignx.AssignxServer.domain.department.repository;

import com.assignx.AssignxServer.domain.department.entity.Department;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByCollegeAndMajor(@NotBlank String college, @NotBlank String major);

    List<Department> findByMajorContaining(String major);
}
