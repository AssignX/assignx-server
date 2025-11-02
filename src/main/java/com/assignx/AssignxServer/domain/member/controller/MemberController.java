package com.assignx.AssignxServer.domain.member.controller;

import com.assignx.AssignxServer.domain.member.dto.MemberResDTO;
import com.assignx.AssignxServer.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "Member", description = "멤버 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/search/employee")
    @Operation(summary = "직원 조회", description = "직원 정보를 조회합니다.")
    public ResponseEntity<List<MemberResDTO>> getAllEmployeesByNameOrDepartment(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long departmentId) {
        List<MemberResDTO> res = memberService.getAllEmployeesByNameOrDepartment(name, departmentId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/search/professor")
    @Operation(summary = "교수 조회", description = "교수 정보를 조회합니다.")
    public ResponseEntity<List<MemberResDTO>> getAllProfessorsByName(@RequestParam String name) {
        List<MemberResDTO> res = memberService.getAllProfessorsByName(name);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/professor")
    @Operation(summary = "특정 학과 소속 교수 목록 조회")
    public ResponseEntity<List<MemberResDTO>> getAllProfessorsByDepartment(@RequestParam Long departmentId) {
        List<MemberResDTO> res = memberService.getAllProfessorsByDepartment(departmentId);
        return ResponseEntity.ok(res);
    }
}
