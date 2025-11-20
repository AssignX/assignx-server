package com.assignx.AssignxServer.global.common.service;

import com.assignx.AssignxServer.domain.course.service.CourseService;
import com.assignx.AssignxServer.domain.exam.entity.ExamType;
import com.assignx.AssignxServer.domain.exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupTask {

    private final CourseService courseService;
    private final ExamService examService;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        courseService.getCourseListFromSY("2025", "2", "1O02");
        examService.createExamObjects("2025", "2", ExamType.MID);
    }
}