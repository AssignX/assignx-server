package com.assignx.AssignxServer.domain.examPeriod.service;

import com.assignx.AssignxServer.domain.examPeriod.dto.ExamPeriodReqDTO;
import com.assignx.AssignxServer.domain.examPeriod.dto.ExamPeriodResDTO;
import com.assignx.AssignxServer.domain.examPeriod.entity.ExamPeriod;
import com.assignx.AssignxServer.domain.examPeriod.repository.ExamPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExamPeriodService {

    private final ExamPeriodRepository examPeriodRepository;

    public ExamPeriodResDTO createExamPeriod(ExamPeriodReqDTO dto) {
        ExamPeriod existing = examPeriodRepository.findByYearAndSemester(dto.year(), dto.semester());
        ExamPeriod examPeriod;
        if (existing == null) {
            examPeriod = examPeriodRepository.save(dto.toEntity());
        } else {
            examPeriod = existing.update(dto);
        }
        return ExamPeriodResDTO.fromEntity(examPeriod);
    }

    public ExamPeriodResDTO getExamPeriod(String year, String semester) {
        ExamPeriod existing = examPeriodRepository.findByYearAndSemester(year, semester);
        return ExamPeriodResDTO.fromEntity(existing);
    }
}
