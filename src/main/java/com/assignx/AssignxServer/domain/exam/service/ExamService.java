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

    private final ExamPeriodService examPeriodService;
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;

    public List<ExamResDTO> firstApply(ExamFirstReqDTO dto) {
        examPeriodService.checkInApplyPeriod(dto.examType(), "1차");
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
        examPeriodService.checkInApplyPeriod(dto.examType(), "2차");
        return null;
    }

    public List<ExamResDTO> searchExam(String year, String semester, Long roomId, Long departmentId, Long professorId) {
        return null;
    }

    public void createExamObjects(String year, String semester, ExamType examType) {
        List<Course> courses = courseRepository.findAllByYearAndSemester(year, semester);
        for (Course course : courses) {
            int professorCnt = course.getProfessorName().split(",").length;
            // TODO 실제로는 이름도 같은지 체크해야하지만 우선은 간단하게 size만 비교
            if (professorCnt == course.getProfessors().size()) {
                Exam exam = Exam.builder()
                        .course(course)
                        .examTime(course.getCourseTime())
                        .examType(examType)
                        .examAssigned(ExamAssigned.NOT_YET)
                        .examRoom(course.getRoom())
                        .build();
                examRepository.save(exam);
            }
        }
    }
}
