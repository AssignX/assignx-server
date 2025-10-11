package com.assignx.AssignxServer.domain.course.service;

import com.assignx.AssignxServer.domain.building.service.BuildingService;
import com.assignx.AssignxServer.domain.course.dto.CourseCreateReqDTO;
import com.assignx.AssignxServer.domain.course.entity.Course;
import com.assignx.AssignxServer.domain.course.exception.CourseExceptionUtils;
import com.assignx.AssignxServer.domain.course.repository.CourseRepository;
import com.assignx.AssignxServer.domain.courseProfessor.entity.CourseProfessor;
import com.assignx.AssignxServer.domain.courseProfessor.repository.CourseProfessorRepository;
import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.department.service.DepartmentService;
import com.assignx.AssignxServer.domain.member.entity.Member;
import com.assignx.AssignxServer.domain.member.service.MemberService;
import com.assignx.AssignxServer.domain.room.entity.Room;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final MemberService memberService;
    private final DepartmentService departmentService;
    private final BuildingService buildingService;

    private final CourseRepository courseRepository;
    private final CourseProfessorRepository courseProfessorRepository;

    public void getCourseListFromSY(String searchYear, String searchSemester, String searchDepartmentCode) {
        Map<String, Object> searchBody = new HashMap<>();
        searchBody.put("estblYear", searchYear);
        if (searchSemester.equals("1")) {
            searchBody.put("estblSmstrSctcd", "CMBS001400001");  // 1학기
        } else if (searchSemester.equals("2")) {
            searchBody.put("estblSmstrSctcd", "CMBS001400002");  // 2학기
        } else {
            throw CourseExceptionUtils.UnknownSemester(searchSemester);
        }
        searchBody.put("estblDprtnCd", searchDepartmentCode); // TODO 학과 별 code 수집하기
        searchBody.put("isApi", "Y");
        searchBody.put("lctreLnggeSctcd", "ko");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("search", searchBody);

        WebClient webClient = WebClient.builder()
                .baseUrl("https://knuin.knu.ac.kr")
                .defaultHeader("Content-Type", "application/json")
                .build();

        Mono<Map<String, Object>> resultMono = webClient.post()
                .uri("/public/web/stddm/lsspr/syllabus/lectPlnInqr/selectListLectPlnInqr")
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        Map<String, Object> responseBody = resultMono.block();
        List<Map<String, Object>> courseList = (List<Map<String, Object>>) responseBody.get("data");

        for (Map<String, Object> course : courseList) {
            String courseName = course.get("sbjetNm").toString();
            String courseCode = course.get("crseNo").toString();
            String courseTime = course.get("lssnsTimeInfo").toString();
            int enrolledCount = (int) course.get("appcrCnt");  // 수강 정원: attlcPrscpCnt, 수강 신청 인원: appcrCnt
            String semester = getSemesterFromSYResponse(course.get("estblYear"), course.get("estblSmstrSctcd"));
            String professorName = course.get("totalPrfssNm").toString();  // "정창수" or  "하이랜드,크리스"
            List<Member> professors = memberService.getProfessorsFromSYResponse(course.get("totalPrfssNm"));
            Room room = buildingService.getBuildingFromSYResponse(course.get("lctrmInfo"), course.get("rmnmCd"));
            Department department = departmentService.getDepartmentFromSYResponse(course.get("estblDprtnNm"));

            // FIXME 추후 예외 처리
            if (professors == null || room == null || department == null) {
                continue;
            }

            CourseCreateReqDTO dto = CourseCreateReqDTO.builder()
                    .courseName(courseName)
                    .courseCode(courseCode)
                    .courseTime(courseTime)
                    .enrolledCount(enrolledCount)
                    .semester(semester)
                    .professorName(professorName)
                    .professors(professors)
                    .room(room)
                    .department(department)
                    .build();

            // Course 객체 저장
            Course savedCourse = courseRepository.save(dto.toEntity());

            // CourseProfessor 관계 엔티티 생성 및 저장
            for (Member professor : dto.professors()) {
                CourseProfessor courseProfessor = CourseProfessor.builder()
                        .professor(professor)
                        .course(savedCourse)
                        .build();

                courseProfessorRepository.save(courseProfessor);
            }
        }
    }

    private String getSemesterFromSYResponse(Object estblYear, Object estblSmstrSctcd) {
        if (estblSmstrSctcd.toString().equals("CMBS001400001")) {
            return estblYear + "-1";
        } else if (estblSmstrSctcd.toString().equals("CMBS001400002")) {
            return estblYear + "-2";
        }
        // TODO 계절학기도 추가
        throw CourseExceptionUtils.SemesterParsingFailed();
    }


}
