package com.assignx.AssignxServer.domain.exam.entity;

import com.assignx.AssignxServer.domain.course.entity.Course;
import com.assignx.AssignxServer.domain.exam.dto.ExamFirstReqDTO;
import com.assignx.AssignxServer.domain.room.entity.Room;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column(nullable = false)
    private ExamType examType;

    @Setter
    @Column(nullable = false)
    private ExamAssigned examAssigned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room examRoom;

    @Builder
    public Exam(Course course, ExamType examType, ExamAssigned examAssigned) {
        this.course = course;
        this.examType = examType;
        this.examAssigned = examAssigned;
    }

    public Exam firstApply(ExamFirstReqDTO dto) {
        if (dto.isApply()) {
            this.startTime = dto.startTime();
            this.endTime = dto.endTime();
            this.examAssigned = ExamAssigned.COMPLETED_FIRST;
            this.examRoom = this.course.getRoom();
        } else {
            this.startTime = null;
            this.endTime = null;
            this.examAssigned = ExamAssigned.NOT_YET;
        }
        return this;
    }

    public void secondApply(LocalDateTime startTime, LocalDateTime endTime, Room examRoom) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.examRoom = examRoom;
    }

}
