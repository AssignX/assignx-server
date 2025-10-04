package com.assignx.AssignxServer.domain.department.controller;

import com.assignx.AssignxServer.domain.department.dto.DepartmentCreateReqDTO;
import com.assignx.AssignxServer.domain.department.dto.DepartmentListResDTO;
import com.assignx.AssignxServer.domain.department.dto.DepartmentReqDTO;
import com.assignx.AssignxServer.domain.department.dto.DepartmentResDTO;
import com.assignx.AssignxServer.domain.department.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/department/admin")
@Tag(name = "Department", description = "학과 API")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping()
    @Operation(summary = "학과 추가", description = "새로운 학과 정보를 추가합니다.")
    public ResponseEntity<DepartmentResDTO> addDepartment(@Valid @RequestBody DepartmentCreateReqDTO dto) {
        DepartmentResDTO res = departmentService.addDepartment(dto);
        return ResponseEntity.ok(res);
    }

    @GetMapping()
    @Operation(summary = "학과 목록 조회", description = "학과 목록을 조회합니다.")
    public ResponseEntity<List<DepartmentListResDTO>> getAllDepartments() {
        List<DepartmentListResDTO> res = departmentService.getAllDepartments();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{departmentId}")
    @Operation(summary = "학과 상세 조회", description = "특정 학과에 대한 정보를 조회합니다.")
    public ResponseEntity<DepartmentResDTO> getDepartment(
            @PathVariable("departmentId") Long departmentId) {
        DepartmentResDTO res = departmentService.getDepartment(departmentId);
        return ResponseEntity.ok(res);
    }

    @PutMapping()
    @Operation(summary = "학과 수정", description = "특정 학과에 대한 정보를 수정합니다.")
    public ResponseEntity<DepartmentResDTO> updateDepartment(@Valid @RequestBody DepartmentReqDTO dto) {
        DepartmentResDTO res = departmentService.updateDepartment(dto);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{departmentId}")
    @Operation(summary = "학과 삭제", description = "특정 학과에 대한 정보를 삭제합니다.")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("departmentId") Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok().build();
    }
}
