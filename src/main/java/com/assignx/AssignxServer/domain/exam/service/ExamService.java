package com.assignx.AssignxServer.domain.exam.service;

import com.assignx.AssignxServer.domain.exam.dto.ExamFirstReqDTO;
import com.assignx.AssignxServer.domain.exam.dto.ExamResDTO;
import com.assignx.AssignxServer.domain.exam.dto.ExamSecondReqDTO;
import com.assignx.AssignxServer.domain.exam.entity.Exam;
import com.assignx.AssignxServer.domain.exam.exception.ExamExceptionUtils;
import com.assignx.AssignxServer.domain.exam.repository.ExamRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExamService {

    private final ExamRepository examRepository;

    public List<ExamResDTO> firstApply(ExamFirstReqDTO dto) {
        if (dto.examIds().isEmpty()) {
            throw ExamExceptionUtils.FirstApplyEmpty();
        }
        List<Exam> exams = examRepository.findAllById(dto.examIds());
        for (Exam exam : exams) {
            exam.firstApply(dto.isApply());
        }
        return exams.stream().map(ExamResDTO::fromEntity).collect(Collectors.toList());
    }

    public List<ExamResDTO> secondApply(@Valid ExamSecondReqDTO dto) {
        return null;
    }

    public List<ExamResDTO> searchExam(String year, String semester, Long roomId, Long departmentId, Long professorId) {
        return null;
    }
}
