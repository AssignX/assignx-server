package com.assignx.AssignxServer.domain.member.service;

import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.department.exception.DepartmentExceptionUtils;
import com.assignx.AssignxServer.domain.department.repository.DepartmentRepository;
import com.assignx.AssignxServer.domain.member.dto.MemberResDTO;
import com.assignx.AssignxServer.domain.member.entity.Member;
import com.assignx.AssignxServer.domain.member.entity.Role;
import com.assignx.AssignxServer.domain.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;

    public List<MemberResDTO> getAllEmployeesByNameOrDepartment(String name, Long departmentId) {
        // 학과 존재 확인
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentExceptionUtils::DepartmentNotExist);

        // Role, name, departmentId로 EMPLOYEE 목록 조회
        List<Member> members = memberRepository.findByRoleAndNameAndDepartment(Role.EMPLOYEE, name, department);
        return members.stream().map(MemberResDTO::fromEntity).toList();
    }

    public List<MemberResDTO> getAllProfessorsByName(String name) {
        // Role, name으로 PROFESSOR 목록 조회
        List<Member> members = memberRepository.findByRoleAndName(Role.EMPLOYEE, name);
        return members.stream().map(MemberResDTO::fromEntity).toList();
    }
}
