package com.assignx.AssignxServer.domain.examPeriod.entity;

import com.assignx.AssignxServer.domain.examPeriod.dto.ExamPeriodReqDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExamPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private LocalDateTime midStartDateTime;

    @Column(nullable = false)
    private LocalDateTime midEndDateTime;

    @Column(nullable = false)
    private LocalDateTime finalStartDateTime;

    @Column(nullable = false)
    private LocalDateTime finalEndDateTime;

    @Builder
    public ExamPeriod(String year, String semester, LocalDateTime midStartDateTime, LocalDateTime midEndDateTime,
                      LocalDateTime finalStartDateTime, LocalDateTime finalEndDateTime) {
        this.year = year;
        this.semester = semester;
        this.midStartDateTime = midStartDateTime;
        this.midEndDateTime = midEndDateTime;
        this.finalStartDateTime = finalStartDateTime;
        this.finalEndDateTime = finalEndDateTime;
    }

    public ExamPeriod update(ExamPeriodReqDTO dto) {
        this.midStartDateTime = dto.midStartDateTime();
        this.midEndDateTime = dto.midEndDateTime();
        this.finalStartDateTime = dto.finalStartDateTime();
        this.finalEndDateTime = dto.finalEndDateTime();
        return this;
    }

}
