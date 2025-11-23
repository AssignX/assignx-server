package com.assignx.AssignxServer.domain.exam.service;

import com.assignx.AssignxServer.domain.course.dto.CourseResDTO;
import com.assignx.AssignxServer.domain.course.entity.Course;
import com.assignx.AssignxServer.domain.course.repository.CourseRepository;
import com.assignx.AssignxServer.domain.courseProfessor.entity.CourseProfessor;
import com.assignx.AssignxServer.domain.exam.dto.ExamFirstReqDTO;
import com.assignx.AssignxServer.domain.exam.dto.ExamResDTO;
import com.assignx.AssignxServer.domain.exam.dto.ExamSecondReqDTO;
import com.assignx.AssignxServer.domain.exam.entity.Exam;
import com.assignx.AssignxServer.domain.exam.entity.ExamAssigned;
import com.assignx.AssignxServer.domain.exam.entity.ExamType;
import com.assignx.AssignxServer.domain.exam.exception.ExamExceptionUtils;
import com.assignx.AssignxServer.domain.exam.repository.ExamRepository;
import com.assignx.AssignxServer.domain.examPeriod.service.ExamPeriodService;
import com.assignx.AssignxServer.domain.member.entity.Member;
import com.assignx.AssignxServer.domain.room.entity.Room;
import com.assignx.AssignxServer.domain.room.exception.RoomExceptionUtils;
import com.assignx.AssignxServer.domain.room.repository.RoomRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
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

        log.info("신청하는 시험: {} : {} ~ {}", dto.examId(), dto.startTime(), dto.endTime());

        for (Exam e : overlapped) {
            log.info("겹치는 시험: {} : {} ~ {}", e.getId(), e.getStartTime(), e.getEndTime());
        }

        // 우선순위 계산
        ExamAssigned priority = determineAssignmentStatus(exam, overlapped);
        exam.setExamAssigned(priority);
        examRepository.save(exam);

        log.info("ExamID {}가 2차 신청하였습니다. (신청값: {})", exam.getId(), exam.getExamAssigned());

        return ExamResDTO.fromEntity(exam);
    }

    private ExamAssigned determineAssignmentStatus(Exam exam, List<Exam> overlaps) {
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
            return ExamAssigned.WAITING_LOW_PRIORITY;
        } else {
            // 기존에 WAITING_HIGH_PRIORITY 였던 시험을 WAITING_LOW_PRIORITY로 변경
            for (Exam e : overlaps) {
                if (e.getExamAssigned() == ExamAssigned.WAITING_HIGH_PRIORITY) {
                    e.setExamAssigned(ExamAssigned.WAITING_LOW_PRIORITY);
                    examRepository.save(e);
                    break;
                }
            }
            return ExamAssigned.WAITING_HIGH_PRIORITY;
        }
    }

    /**
     * 다양한 검색 조건(연도/학기/강의실/학과/교수ID)을 기반으로 시험 목록을 조회합니다.
     *
     * @param year         (선택) 개설연도
     * @param semester     (선택) 개설학기
     * @param roomId       (선택) 강의실 ID
     * @param departmentId (선택) 학과 ID
     * @param professorId  (선택) 교수 ID
     * @return 조회된 모든 {@link CourseResDTO} 객체 리스트.
     */
    public List<ExamResDTO> searchExam(String year, String semester, Long roomId, Long departmentId, Long professorId) {
        log.info("year: {}, semester: {}, professorId: {}", year, semester, professorId);
        Specification<Exam> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Exam → Course 조인
            Join<Exam, Course> courseJoin = root.join("course", JoinType.INNER);

            // 개설연도
            if (year != null && !year.isBlank()) {
                predicates.add(cb.equal(courseJoin.get("year"), year));
            }

            // 학기
            if (semester != null && !semester.isBlank()) {
                predicates.add(cb.equal(courseJoin.get("semester"), semester));
            }

            // 강의실
            if (roomId != null) {
                predicates.add(cb.equal(root.join("examRoom", JoinType.LEFT).get("id"), roomId));
            }

            // 학과
            if (departmentId != null) {
                predicates.add(cb.equal(courseJoin.join("department", JoinType.LEFT).get("id"), departmentId));
            }

            // 교수 조건
            if (professorId != null) {
                Join<Course, CourseProfessor> cpJoin = courseJoin.join("professors", JoinType.LEFT);
                Join<CourseProfessor, Member> professorJoin = cpJoin.join("professor", JoinType.LEFT);
                predicates.add(cb.equal(professorJoin.get("id"), professorId));
            }

            query.distinct(true); // 교수 조인으로 인한 중복 제거
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<Exam> exams = examRepository.findAll(spec);
        return exams.stream().map(ExamResDTO::fromEntity).toList();
    }

    @Transactional
    public void createExamObjects(String year, String semester, ExamType examType) {
        List<Course> courses = courseRepository.findAllByYearAndSemester(year, semester);
        for (Course course : courses) {
            int professorCnt = course.getProfessorName().split(",").length;
            // TODO 실제로는 이름도 같은지 체크해야하지만 우선은 간단하게 size만 비교
            if (professorCnt == course.getProfessors().size()) {
                // 이미 시험 객체가 존재한다면 continue
                Exam existingExam = examRepository.findByCourseAndExamType(course, examType);
                if (existingExam != null) {
                    continue;
                }

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

    @Transactional
    public ExamResDTO confirmExam(ExamSecondReqDTO dto) {
        log.info("confirm with exam id: {}", dto.examId());
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
        ExamAssigned priority = determineAssignmentStatus(exam, overlapped);
        if (priority != ExamAssigned.WAITING_HIGH_PRIORITY) {
            throw ExamExceptionUtils.ExamScheduleConflict();
        }
        exam.setExamAssigned(ExamAssigned.COMPLETED_SECOND);
        examRepository.save(exam);

        log.info("ExamID {}가 수정되었습니다.", exam.getId());

        return ExamResDTO.fromEntity(exam);
    }
}
