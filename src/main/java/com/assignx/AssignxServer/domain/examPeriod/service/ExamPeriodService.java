package com.assignx.AssignxServer.domain.examPeriod.service;

import com.assignx.AssignxServer.domain.exam.entity.ExamType;
import com.assignx.AssignxServer.domain.examPeriod.dto.ExamPeriodReqDTO;
import com.assignx.AssignxServer.domain.examPeriod.dto.ExamPeriodResDTO;
import com.assignx.AssignxServer.domain.examPeriod.entity.ExamPeriod;
import com.assignx.AssignxServer.domain.examPeriod.exception.ExamPeriodException;
import com.assignx.AssignxServer.domain.examPeriod.exception.ExamPeriodExceptionUtils;
import com.assignx.AssignxServer.domain.examPeriod.repository.ExamPeriodRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExamPeriodService {

    private final ExamPeriodRepository examPeriodRepository;

    /**
     * 시험 신청 기간을 추가합니다.
     *
     * @param dto 시험 신청 기간 생성에 필요한 데이터를 담고 있는 {@link ExamPeriodReqDTO} 객체.
     * @return 성공적으로 저장된 {@link ExamPeriodResDTO} 객체.
     */
    public ExamPeriodResDTO createExamPeriod(ExamPeriodReqDTO dto) {
        Optional<ExamPeriod> existing = examPeriodRepository.findByYearAndSemester(dto.year(), dto.semester());
        ExamPeriod examPeriod;
        if (existing.isPresent()) {
            examPeriod = existing.get();
            examPeriod = examPeriod.update(dto);
        } else {
            examPeriod = examPeriodRepository.save(dto.toEntity());
        }
        return ExamPeriodResDTO.fromEntity(examPeriod);
    }

    /**
     * 조건에 맞는 시험 신청 기간을 조회합니다.
     *
     * @param year     조회할 연도
     * @param semester 조회할 학기
     * @return 조회된 {@link ExamPeriodResDTO} 객체.
     */
    public ExamPeriodResDTO getExamPeriod(String year, String semester) {
        ExamPeriod existing = examPeriodRepository.findByYearAndSemester(year, semester)
                .orElseThrow(ExamPeriodExceptionUtils::ExamPeriodNotFound);
        return ExamPeriodResDTO.fromEntity(existing);
    }

    /**
     * 현재 날짜가 시험 신청 기간 내에 속하는지 확인합니다.
     *
     * @param examType 중간/기말 여부
     * @param period   1차/2차 여부
     * @throws ExamPeriodException 현재 날짜가 시험 신청 기간 내에 속하지 않으면 예외처리.
     */
    public void checkInApplyPeriod(ExamType examType, String period) {
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        int month = now.getMonthValue();

        String semester = getSemesterOrThrow(month);

        ExamPeriod examPeriod = examPeriodRepository.findByYearAndSemester(year, semester)
                .orElseThrow(ExamPeriodExceptionUtils::ExamPeriodNotFound);

        LocalDateTime start = getStartDateTime(examPeriod, examType, period);
        LocalDateTime end = getEndDateTime(examPeriod, examType, period);

        if (!(now.isAfter(start) && now.isBefore(end))) {
            throw ExamPeriodExceptionUtils.NotInApplyPeriod();
        }
    }

    private String getSemesterOrThrow(int month) {
        if (month >= 3 && month <= 6) {
            return "1";
        }
        if (month >= 9) {
            return "2";
        }
        throw ExamPeriodExceptionUtils.NotInSemester();
    }

    private LocalDateTime getStartDateTime(ExamPeriod examPeriod, ExamType examType, String period) {
        return switch (examType) {
            case MID -> switch (period) {
                case "1차" -> examPeriod.getMidFirstStartDateTime();
                case "2차" -> examPeriod.getMidSecondStartDateTime();
                default -> throw ExamPeriodExceptionUtils.UnknownExamPeriod(period);
            };
            case FINAL -> switch (period) {
                case "1차" -> examPeriod.getFinalFirstStartDateTime();
                case "2차" -> examPeriod.getFinalSecondStartDateTime();
                default -> throw ExamPeriodExceptionUtils.UnknownExamPeriod(period);
            };
        };
    }

    private LocalDateTime getEndDateTime(ExamPeriod examPeriod, ExamType examType, String period) {
        return switch (examType) {
            case MID -> switch (period) {
                case "1차" -> examPeriod.getMidFirstEndDateTime();
                case "2차" -> examPeriod.getMidSecondEndDateTime();
                default -> throw ExamPeriodExceptionUtils.UnknownExamPeriod(period);
            };
            case FINAL -> switch (period) {
                case "1차" -> examPeriod.getFinalFirstEndDateTime();
                case "2차" -> examPeriod.getFinalSecondEndDateTime();
                default -> throw ExamPeriodExceptionUtils.UnknownExamPeriod(period);
            };
        };
    }
}
