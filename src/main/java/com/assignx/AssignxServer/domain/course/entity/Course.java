package com.assignx.AssignxServer.domain.course.entity;

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
import lombok.AccessLevel;
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
    private String semester;

    @Column
    private String professorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

}
