package com.assignx.AssignxServer.domain.exam.entity;

import com.assignx.AssignxServer.domain.course.entity.Course;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String examTime;

    @Column(nullable = false)
    private ExamType examType;

    @Column(nullable = false)
    private ExamAssigned examAssigned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room examRoom;

    @Builder
    public Exam(Course course, String examTime, ExamType examType, ExamAssigned examAssigned, Room examRoom) {
        this.course = course;
        this.examTime = examTime;
        this.examType = examType;
        this.examAssigned = examAssigned;
        this.examRoom = examRoom;
    }

    public void firstApply(boolean isApply) {
        if (isApply) {
            this.examAssigned = ExamAssigned.COMPLETED_FIRST;
        } else {
            this.examAssigned = ExamAssigned.NOT_YET;
        }
    }

}
