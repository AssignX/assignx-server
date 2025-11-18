package com.assignx.AssignxServer.domain.member.service;

import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.department.exception.DepartmentExceptionUtils;
import com.assignx.AssignxServer.domain.department.repository.DepartmentRepository;
import com.assignx.AssignxServer.domain.member.dto.MemberResDTO;
import com.assignx.AssignxServer.domain.member.entity.Member;
import com.assignx.AssignxServer.domain.member.entity.Role;
import com.assignx.AssignxServer.domain.member.repository.MemberRepository;
import java.util.ArrayList;
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

    /**
     * 이름 혹은 소속으로 직원을 검색합니다.
     *
     * @param name         조회할 직원의 이름.
     * @param departmentId 조회할 직원의 학과 ID.
     * @return 조회된 모든 {@link MemberResDTO} 객체 리스트.
     */
    public List<MemberResDTO> getAllEmployeesByNameOrDepartment(String name, Long departmentId) {
        List<Member> members = new ArrayList<>();

        // 이름으로 조회한 경우
        if (name != null && !name.isBlank()) {
            members = memberRepository.findByRoleAndName(Role.EMPLOYEE, name);
        }
        // 소속으로 조회한 경우
        else if (departmentId != null) {
            // 학과 존재 확인
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(DepartmentExceptionUtils::DepartmentNotExist);
            members = memberRepository.findByRoleAndDepartment(Role.EMPLOYEE, department);
        }

        return members.stream().map(MemberResDTO::fromEntity).toList();
    }

    /**
     * 이름으로 교수를 검색합니다.
     *
     * @param name         조회할 교수의 이름.
     * @param departmentId 조회할 교수의 학과 ID.
     * @return 조회된 모든 {@link MemberResDTO} 객체 리스트.
     */
    public List<MemberResDTO> getAllProfessorsByNameOrDepartment(String name, Long departmentId) {
        List<Member> members = new ArrayList<>();

        // 이름으로 조회한 경우
        if (name != null && !name.isBlank()) {
            members = memberRepository.findByRoleAndName(Role.PROFESSOR, name);
        }
        // 소속으로 조회한 경우
        else if (departmentId != null) {
            // 학과 존재 확인
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(DepartmentExceptionUtils::DepartmentNotExist);
            members = memberRepository.findByRoleAndDepartment(Role.PROFESSOR, department);
        }

        return members.stream().map(MemberResDTO::fromEntity).toList();
    }

    /**
     * SY에서 조회한 교수명을 실제 교수 객체와 매핑합니다.
     *
     * @param totalPrfssNm 조회할 교수 이름 문자열.
     * @return 조회된 모든 {@link Member} 객체 리스트.
     */
    public List<Member> getProfessorsFromSYResponse(Object totalPrfssNm) {
        String[] nameList = totalPrfssNm.toString().split(",");
        List<Member> professors = new ArrayList<>();
        for (String name : nameList) {
            // FIXME 교수 매핑 테스트를 위해 특정 교수 매핑을 건너뜀
            if (name.equals("고석주") || name.equals("정창수")) {
                continue;
            }
            List<Member> searchRes = memberRepository.findByRoleAndName(Role.PROFESSOR, name);
            // 검색된 교수가 한 명 뿐이어야 바로 매핑 가능
            if (searchRes.size() == 1) {
                professors.add(searchRes.get(0));
            } else {
                return null;
            }
        }
        return professors;
    }

}
