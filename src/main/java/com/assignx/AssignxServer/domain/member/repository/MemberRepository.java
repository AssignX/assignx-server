package com.assignx.AssignxServer.domain.member.repository;

import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.member.entity.Member;
import com.assignx.AssignxServer.domain.member.entity.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByRoleAndName(Role role, String name);

    List<Member> findByRoleAndDepartment(Role role, Department department);

    Optional<Member> findByIdNumber(String idNumber);
}
