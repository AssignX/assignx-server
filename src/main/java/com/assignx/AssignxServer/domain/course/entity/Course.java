package com.assignx.AssignxServer.domain.course.entity;

import com.assignx.AssignxServer.domain.courseProfessor.entity.CourseProfessor;
import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.room.entity.Room;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(nullable = false)
    private String courseName;

    @Column(nullable = false)
    private String courseCode;

    @Column(nullable = false)
    private String courseTime;

    @Column(nullable = false)
    private int enrolledCount;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false)
    private String semester;

    @Column
    private String professorName;

    @OneToMany(mappedBy = "course")
    private List<CourseProfessor> professors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @Builder
    public Course(String courseName, String courseCode, String courseTime, int enrolledCount, String year,
                  String semester,
                  String professorName, Room room, Department department) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseTime = courseTime;
        this.enrolledCount = enrolledCount;
        this.year = year;
        this.semester = semester;
        this.professorName = professorName;
        this.room = room;
        this.department = department;
    }

}
