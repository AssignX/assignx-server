package com.assignx.AssignxServer.domain.member.repository;

import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.member.entity.Member;
import com.assignx.AssignxServer.domain.member.entity.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByRoleAndNameAndDepartment(Role role, String name, Department department);

    List<Member> findByRoleAndName(Role role, String name);
}
