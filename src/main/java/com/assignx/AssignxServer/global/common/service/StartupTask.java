package com.assignx.AssignxServer.global.common.service;

import com.assignx.AssignxServer.domain.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupTask {

    private final CourseService courseService;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        courseService.getCourseListFromSY("2025", "2", "1O02");
    }
}