package com.assignx.AssignxServer.domain.member.controller;

import com.assignx.AssignxServer.domain.member.dto.MemberResDTO;
import com.assignx.AssignxServer.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name = "Member", description = "멤버 API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/search/employee")
    public ResponseEntity<List<MemberResDTO>> getAllEmployeesByNameOrDepartment(
            @RequestParam String name, Long departmentId) {
        List<MemberResDTO> res = memberService.getAllEmployeesByNameOrDepartment(name, departmentId);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/search/professor")
    public ResponseEntity<List<MemberResDTO>> getAllProfessorsByName(@RequestParam String name) {
        List<MemberResDTO> res = memberService.getAllProfessorsByName(name);
        return ResponseEntity.ok(res);
    }
}
