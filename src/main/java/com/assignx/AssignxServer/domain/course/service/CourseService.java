package com.assignx.AssignxServer.domain.course.service;

import com.assignx.AssignxServer.domain.building.service.BuildingService;
import com.assignx.AssignxServer.domain.course.dto.CourseCreateReqDTO;
import com.assignx.AssignxServer.domain.course.dto.CourseProfessorMapReqDTO;
import com.assignx.AssignxServer.domain.course.dto.CourseResDTO;
import com.assignx.AssignxServer.domain.course.entity.Course;
import com.assignx.AssignxServer.domain.course.exception.CourseExceptionUtils;
import com.assignx.AssignxServer.domain.course.repository.CourseRepository;
import com.assignx.AssignxServer.domain.courseProfessor.entity.CourseProfessor;
import com.assignx.AssignxServer.domain.courseProfessor.repository.CourseProfessorRepository;
import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.department.service.DepartmentService;
import com.assignx.AssignxServer.domain.member.entity.Member;
import com.assignx.AssignxServer.domain.member.exception.MemberExceptionUtils;
import com.assignx.AssignxServer.domain.member.repository.MemberRepository;
import com.assignx.AssignxServer.domain.member.service.MemberService;
import com.assignx.AssignxServer.domain.room.entity.Room;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final MemberRepository memberRepository;

    /**
     * SY에서 강좌 목록을 조회하여 저장합니다.
     *
     * @param searchYear           조회할 개설연도
     * @param searchSemester       조회할 개설학기
     * @param searchDepartmentCode 조회할 학과코드
     */
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
                    .year(searchYear)
                    .semester(searchSemester)
                    .professorName(professorName)
                    .professors(professors)
                    .room(room)
                    .department(department)
                    .build();

            // Course 중복 검사
            Optional<Course> existingCourse = courseRepository.findByCourseCodeAndSemester(dto.courseCode(),
                    dto.semester());

            if (existingCourse.isPresent()) {
                // FIXME 우선은 중복되는게 있다면 그냥 스킵함
//                throw CourseExceptionUtils.CourseAlreadyExists();
                continue;
            }

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

    /**
     * 개설연도, 개설학기, 강의실 번호를 기준으로 과목 목록을 조회합니다.
     *
     * @param year       조회할 개설연도
     * @param semester   조회할 개설학기
     * @param roomNumber 조회할 강의실 번호
     * @return 조회된 모든 {@link CourseResDTO} 객체 리스트.
     */
    public List<CourseResDTO> getCourseByYearAndSemesterAndRoomNumber(String year, String semester, String roomNumber) {
        List<Course> courses = courseRepository.findByYearAndSemesterAndRoom_RoomNumber(year, semester, roomNumber);
        return courses.stream().map(CourseResDTO::fromEntity).toList();
    }

    /**
     * 개설학과, 교수명을 기준으로 과목 목록을 조회합니다.
     *
     * @param major         조회할 학과명
     * @param professorName 조회할 교수명
     * @return 조회된 모든 {@link CourseResDTO} 객체 리스트.
     */
    public List<CourseResDTO> getCourseByDepartmentAndProfessorName(String major, String professorName) {
        List<Course> courses = courseRepository.findCoursesByMajorAndProfessorName(major, professorName);
        return courses.stream().map(CourseResDTO::fromEntity).toList();
    }

    /**
     * 개설학과, 교수 ID를 기준으로 과목 목록을 조회합니다.
     *
     * @param major       조회할 학과명
     * @param professorId 조회할 교수 ID
     * @return 조회된 모든 {@link CourseResDTO} 객체 리스트.
     */
    public List<CourseResDTO> getCourseByDepartmentAndProfessorId(String major, Long professorId) {
        List<Course> courses = courseRepository.findCoursesByMajorAndProfessorId(major, professorId);
        return courses.stream().map(CourseResDTO::fromEntity).toList();
    }

    /**
     * 특정 강의를 담당하는 교수 정보를 매핑합니다.
     *
     * @param dto 매핑할 강의와 교수 정보를 담고 있는 {@link CourseProfessorMapReqDTO} 객체.
     * @return 매핑된 {@link CourseResDTO} 객체.
     */
    public CourseResDTO mapCourseProfessor(CourseProfessorMapReqDTO dto) {
        Course course = courseRepository.findById(dto.courseId()).orElseThrow(CourseExceptionUtils::CourseNotFound);
        Member professor = memberRepository.findById(dto.professorId())
                .orElseThrow(MemberExceptionUtils::ProfessorNotFound);

        CourseProfessor courseProfessor = CourseProfessor.builder()
                .professor(professor)
                .course(course)
                .build();

        courseProfessorRepository.save(courseProfessor);
        return CourseResDTO.fromEntity(course);
    }
}
