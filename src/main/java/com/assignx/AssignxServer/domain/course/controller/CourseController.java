package com.assignx.AssignxServer.domain.course.controller;

import com.assignx.AssignxServer.domain.course.service.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
@Tag(name = "Course", description = "과목 API")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public void test() {
        courseService.getCourseListFromSY("2025", "2", "1O02");
    }
}
