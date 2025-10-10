package com.assignx.AssignxServer.domain.department.service;

import com.assignx.AssignxServer.domain.department.dto.DepartmentCreateReqDTO;
import com.assignx.AssignxServer.domain.department.dto.DepartmentListResDTO;
import com.assignx.AssignxServer.domain.department.dto.DepartmentReqDTO;
import com.assignx.AssignxServer.domain.department.dto.DepartmentResDTO;
import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.department.exception.DepartmentExceptionUtils;
import com.assignx.AssignxServer.domain.department.repository.DepartmentRepository;
import com.assignx.AssignxServer.domain.member.dto.MemberResDTO;
import com.assignx.AssignxServer.domain.room.dto.RoomResDTO;
import com.assignx.AssignxServer.domain.room.service.RoomService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final RoomService roomService;

    /**
     * 새로운 학과를 추가합니다. ADMIN 권한으로 호출됩니다.
     *
     * @param dto 학과 생성에 필요한 데이터를 담고 있는 {@link DepartmentCreateReqDTO} 객체.
     * @return 성공적으로 저장된 {@link DepartmentResDTO} 객체.
     */
    @Transactional
    public DepartmentResDTO addDepartment(@Valid DepartmentCreateReqDTO dto) {
        // 중복 여부 확인
        departmentRepository.findByCollegeAndMajor(dto.college(), dto.major())
                .ifPresent(b -> {
                    throw DepartmentExceptionUtils.DepartmentAlreadyExists();
                });

        // 학과 생성 및 저장
        Department department = dto.toEntity();
        Department savedDepartment = departmentRepository.save(department);

        // TODO 직원 목록 저장
        List<MemberResDTO> updatedEmployees = new ArrayList<>();

        // 강의실 목록 저장
        List<RoomResDTO> updatedRooms = roomService.updateDepartment(dto.roomIds(), department);

        return DepartmentResDTO.fromEntity(savedDepartment, updatedEmployees, updatedRooms);
    }

    /**
     * 모든 학과를 조회합니다. ADMIN 권한으로 호출됩니다.
     *
     * @return 조회된 모든 {@link DepartmentListResDTO} 객체 리스트.
     */
    public List<DepartmentListResDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(DepartmentListResDTO::fromEntity).toList();
    }

    /**
     * 특정 ID를 가진 학과를 조회합니다. ADMIN 권한으로 호출됩니다.
     *
     * @param departmentId 조회할 학과의 고유 ID.
     * @return 조회된 {@link DepartmentResDTO} 객체.
     */
    public DepartmentResDTO getDepartment(Long departmentId) {
        // 학과 조회
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentExceptionUtils::DepartmentNotExist);

        // TODO 학과에 소속된 직원 목록 조회
        List<MemberResDTO> employees = new ArrayList<>();

        // 학과에서 관리하는 강의실 목록 조회
        List<RoomResDTO> rooms = roomService.getRoomsByDepartment(department);

        return DepartmentResDTO.fromEntity(department, employees, rooms);
    }

    @Transactional
    public DepartmentResDTO updateDepartment(@Valid DepartmentReqDTO dto) {
        // 학과 조회
        Department department = departmentRepository.findById(dto.departmentId())
                .orElseThrow(DepartmentExceptionUtils::DepartmentNotExist);

        // 학과 수정
        department.update(dto);

        // TODO 직원 목록 수정
        List<MemberResDTO> employees = new ArrayList<>();

        // 강의실 수정
        List<RoomResDTO> updatedRooms = roomService.updateDepartment(dto.roomIds(), department);

        return DepartmentResDTO.fromEntity(department, employees, updatedRooms);
    }

    @Transactional
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentExceptionUtils::DepartmentNotExist);

        departmentRepository.delete(department);
    }
}
