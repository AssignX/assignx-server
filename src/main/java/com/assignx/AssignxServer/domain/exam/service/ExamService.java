package com.assignx.AssignxServer.domain.exam.service;

import com.assignx.AssignxServer.domain.course.entity.Course;
import com.assignx.AssignxServer.domain.course.repository.CourseRepository;
import com.assignx.AssignxServer.domain.exam.dto.ExamFirstReqDTO;
import com.assignx.AssignxServer.domain.exam.dto.ExamResDTO;
import com.assignx.AssignxServer.domain.exam.dto.ExamSecondReqDTO;
import com.assignx.AssignxServer.domain.exam.entity.Exam;
import com.assignx.AssignxServer.domain.exam.entity.ExamAssigned;
import com.assignx.AssignxServer.domain.exam.entity.ExamType;
import com.assignx.AssignxServer.domain.exam.exception.ExamExceptionUtils;
import com.assignx.AssignxServer.domain.exam.repository.ExamRepository;
import com.assignx.AssignxServer.domain.examPeriod.service.ExamPeriodService;
import com.assignx.AssignxServer.domain.room.entity.Room;
import com.assignx.AssignxServer.domain.room.exception.RoomExceptionUtils;
import com.assignx.AssignxServer.domain.room.repository.RoomRepository;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExamService {

    private final ExamPeriodService examPeriodService;
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public ExamResDTO firstApply(ExamFirstReqDTO dto) {
        examPeriodService.checkInApplyPeriod(dto.examType(), "1차");
        Exam exam = examRepository.findById(dto.examId()).orElseThrow(ExamExceptionUtils::ExamNotFound);
        exam = examRepository.save(exam.firstApply(dto));
        log.info("ExamID {}가 1차 신청하였습니다. (신청값: {})", exam.getId(), exam.getExamAssigned());
        return ExamResDTO.fromEntity(exam);
    }

    @Transactional
    public ExamResDTO secondApply(@Valid ExamSecondReqDTO dto) {
        examPeriodService.checkInApplyPeriod(dto.examType(), "2차");

        Exam exam = examRepository.findById(dto.examId()).orElseThrow(ExamExceptionUtils::ExamNotFound);
        Room examRoom = roomRepository.findById(dto.examRoomId()).orElseThrow(RoomExceptionUtils::RoomNotExist);

        exam.secondApply(dto.startTime(), dto.endTime(), examRoom);

        // 시간대가 겹치는 시험 조회
        List<Exam> overlapped = examRepository.findAllOverlapping(
                dto.startTime(), dto.endTime()
        );

        // 자기 자신 제외
        overlapped.removeIf(e -> e.getId().equals(exam.getId()));

        // 우선순위 계산
        determineAssignmentStatus(exam, overlapped);
        examRepository.save(exam);

        log.info("ExamID {}가 2차 신청하였습니다. (신청값: {})", exam.getId(), exam.getExamAssigned());

        return ExamResDTO.fromEntity(exam);
    }

    private void determineAssignmentStatus(Exam exam, List<Exam> overlaps) {
        long myScore = exam.getCourse().getEnrolledCount();

        boolean hasHigher = overlaps.stream()
                .peek(e -> {
                    if (e.getCourse().getEnrolledCount() > myScore) {
                        log.info("현재 시험(ExamID: {}, count: {}명) 보다 우선순위가 높은 시험(ExamID: {}, count: {}명)이 존재합니다.",
                                exam.getId(),
                                exam.getCourse().getEnrolledCount(), e.getId(), e.getCourse().getEnrolledCount());
                    }
                })
                .anyMatch(e -> e.getCourse().getEnrolledCount() > myScore);

        if (hasHigher) {
            exam.setExamAssigned(ExamAssigned.WAITING_LOW_PRIORITY);
        } else {
            // 기존에 WAITING_HIGH_PRIORITY 였던 시험을 WAITING_LOW_PRIORITY로 변경
            for (Exam e : overlaps) {
                if (e.getExamAssigned() == ExamAssigned.WAITING_HIGH_PRIORITY) {
                    e.setExamAssigned(ExamAssigned.WAITING_LOW_PRIORITY);
                    examRepository.save(e);
                    break;
                }
            }
            exam.setExamAssigned(ExamAssigned.WAITING_HIGH_PRIORITY);
        }
    }


    public List<ExamResDTO> searchExam(String year, String semester, Long roomId, Long departmentId, Long professorId) {
        return null;
    }

    @Transactional
    public void createExamObjects(String year, String semester, ExamType examType) {
        List<Course> courses = courseRepository.findAllByYearAndSemester(year, semester);
        for (Course course : courses) {
            int professorCnt = course.getProfessorName().split(",").length;
            // TODO 실제로는 이름도 같은지 체크해야하지만 우선은 간단하게 size만 비교
            if (professorCnt == course.getProfessors().size()) {
                Exam exam = Exam.builder()
                        .course(course)
                        .examType(examType)
                        .examAssigned(ExamAssigned.NOT_YET)
                        .build();
                examRepository.save(exam);
            }
        }
    }

    @Transactional
    public void completeSecondExamPeriod(String year, String semester, ExamType examType) {
        List<Exam> exams = examRepository.findAllByYearSemesterAndExamTypeAndAssigned(year, semester, examType,
                ExamAssigned.WAITING_HIGH_PRIORITY);
        exams.forEach(e -> e.setExamAssigned(ExamAssigned.COMPLETED_SECOND));
    }
}
