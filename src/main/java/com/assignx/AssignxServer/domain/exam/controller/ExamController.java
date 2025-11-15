package com.assignx.AssignxServer.domain.exam.controller;

import com.assignx.AssignxServer.domain.exam.dto.ExamFirstReqDTO;
import com.assignx.AssignxServer.domain.exam.dto.ExamResDTO;
import com.assignx.AssignxServer.domain.exam.dto.ExamSecondReqDTO;
import com.assignx.AssignxServer.domain.exam.entity.ExamType;
import com.assignx.AssignxServer.domain.exam.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exam")
@Tag(name = "Exam", description = "시험 API")
public class ExamController {

    private final ExamService examService;

    @PostMapping("/apply/first")
    @Operation(summary = "1차 신청")
    public ResponseEntity<List<ExamResDTO>> firstApply(@Valid @RequestBody ExamFirstReqDTO dto) {
        List<ExamResDTO> res = examService.firstApply(dto);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/apply/second")
    @Operation(summary = "2차 신청")
    public ResponseEntity<List<ExamResDTO>> secondApply(@Valid @RequestBody ExamSecondReqDTO dto) {
        List<ExamResDTO> res = examService.secondApply(dto);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/search")
    @Operation(summary = "시험 목록조회")
    public ResponseEntity<List<ExamResDTO>> searchExam(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long professorId
    ) {
        return ResponseEntity.ok(
                examService.searchExam(year, semester, roomId, departmentId, professorId)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    @Operation(summary = "시험 목록 생성", description = "교수 매핑이 완료된 과목들의 시험 객체를 생성합니다.")
    public ResponseEntity<Void> createExamObjects(
            @RequestParam String year,
            @RequestParam String semester,
            @RequestParam ExamType examtype
    ) {
        examService.createExamObjects(year, semester, examtype);
        return ResponseEntity.ok().build();
    }

}
