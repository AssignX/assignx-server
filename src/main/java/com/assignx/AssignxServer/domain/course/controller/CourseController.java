package com.assignx.AssignxServer.domain.course.controller;

import com.assignx.AssignxServer.domain.course.dto.CourseProfessorMapReqDTO;
import com.assignx.AssignxServer.domain.course.dto.CourseResDTO;
import com.assignx.AssignxServer.domain.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/api/course")
@Tag(name = "Course", description = "과목 API")
public class CourseController {

    private final CourseService courseService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public void test() {
        courseService.getCourseListFromSY("2025", "2", "1O02");
    }

    @GetMapping(value = "/search", params = {"year", "semester", "roomNumber"})
    @Operation(summary = "과목 조회 (시간/장소 기반)", description = "개설연도, 개설학기, 강의실 번호를 기준으로 과목 목록을 조회합니다.")
    public ResponseEntity<List<CourseResDTO>> getCourseByYearAndSemesterAndRoomNumber(@RequestParam String year,
                                                                                      @RequestParam String semester,
                                                                                      @RequestParam String roomNumber) {
        List<CourseResDTO> res = courseService.getCourseByYearAndSemesterAndRoomNumber(year, semester, roomNumber);
        return ResponseEntity.ok(res);
    }

    @GetMapping(value = "/search", params = {"major", "professorName"})
    @Operation(summary = "과목 조회 (학과/교수 기반)", description = "개설학과, 교수명을 기준으로 과목 목록을 조회합니다.")
    public ResponseEntity<List<CourseResDTO>> getCourseByDepartmentAndProfessorName(@RequestParam String major,
                                                                                    @RequestParam String professorName) {
        List<CourseResDTO> res = courseService.getCourseByDepartmentAndProfessorName(major, professorName);
        return ResponseEntity.ok(res);
    }

    @GetMapping(value = "/search", params = {"major", "professorId"})
    @Operation(summary = "과목 조회 (학과/교수 ID 기반)", description = "개설학과, 교수 ID를 기준으로 과목 목록을 조회합니다.")
    public ResponseEntity<List<CourseResDTO>> getCourseByDepartmentAndProfessorId(@RequestParam String major,
                                                                                  @RequestParam Long professorId) {
        List<CourseResDTO> res = courseService.getCourseByDepartmentAndProfessorId(major, professorId);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/map")
    @Operation(summary = "교수 매핑", description = "특정 과목에 담당 교수를 매핑합니다.")
    public ResponseEntity<CourseResDTO> mapCourseProfessor(@Valid @RequestBody CourseProfessorMapReqDTO dto) {
        CourseResDTO res = courseService.mapCourseProfessor(dto);
        return ResponseEntity.ok(res);
    }

}
