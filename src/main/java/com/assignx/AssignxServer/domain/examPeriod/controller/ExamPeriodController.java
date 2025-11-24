package com.assignx.AssignxServer.domain.examPeriod.controller;

import com.assignx.AssignxServer.domain.examPeriod.dto.ExamPeriodReqDTO;
import com.assignx.AssignxServer.domain.examPeriod.dto.ExamPeriodResDTO;
import com.assignx.AssignxServer.domain.examPeriod.service.ExamPeriodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/exam-period")
@Tag(name = "Exam Period", description = "시험 신청 기간 API")
public class ExamPeriodController {

    private final ExamPeriodService examPeriodService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "시험 신청 기간 설정", description = "1차/2차 신청 기간을 설정합니다.")
    public ResponseEntity<ExamPeriodResDTO> createExamPeriod(@RequestBody ExamPeriodReqDTO dto) {
        ExamPeriodResDTO res = examPeriodService.createExamPeriod(dto);
        return ResponseEntity.ok(res);
    }
    
    @GetMapping
    @Operation(summary = "시험 신청 기간 조회", description = "연도, 학기를 이용해 시험 신청 기간을 조회합니다.")
    public ResponseEntity<ExamPeriodResDTO> getExamPeriod(
            @RequestParam String year,
            @RequestParam String semester
    ) {
        ExamPeriodResDTO res = examPeriodService.getExamPeriod(year, semester);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/test")
    @Operation(summary = "테스트용 시험 신청 기간 설정", description = "테스트용 1차/2차 신청 기간을 설정합니다.")
    public ResponseEntity<ExamPeriodResDTO> createTestExamPeriod(@RequestParam String year,
                                                                 @RequestParam String semester,
                                                                 @RequestParam String midFinal,
                                                                 @RequestParam String firstSecond) {
        ExamPeriodResDTO res = examPeriodService.createTestExamPeriod(year, semester, midFinal, firstSecond);
        return ResponseEntity.ok(res);
    }
}
