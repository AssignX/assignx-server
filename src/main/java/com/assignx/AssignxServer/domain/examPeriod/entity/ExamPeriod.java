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
    private LocalDateTime midFirstStartDateTime;

    @Column(nullable = false)
    private LocalDateTime midFirstEndDateTime;

    @Column(nullable = false)
    private LocalDateTime midSecondStartDateTime;

    @Column(nullable = false)
    private LocalDateTime midSecondEndDateTime;

    @Column(nullable = false)
    private LocalDateTime finalFirstStartDateTime;

    @Column(nullable = false)
    private LocalDateTime finalFirstEndDateTime;

    @Column(nullable = false)
    private LocalDateTime finalSecondStartDateTime;

    @Column(nullable = false)
    private LocalDateTime finalSecondEndDateTime;

    @Builder
    public ExamPeriod(String year, String semester,
                      LocalDateTime midFirstStartDateTime,
                      LocalDateTime midFirstEndDateTime,
                      LocalDateTime midSecondStartDateTime,
                      LocalDateTime midSecondEndDateTime,
                      LocalDateTime finalFirstStartDateTime,
                      LocalDateTime finalFirstEndDateTime,
                      LocalDateTime finalSecondStartDateTime,
                      LocalDateTime finalSecondEndDateTime) {
        this.year = year;
        this.semester = semester;
        this.midFirstStartDateTime = midFirstStartDateTime;
        this.midFirstEndDateTime = midFirstEndDateTime;
        this.midSecondStartDateTime = midSecondStartDateTime;
        this.midSecondEndDateTime = midSecondEndDateTime;
        this.finalFirstStartDateTime = finalFirstStartDateTime;
        this.finalFirstEndDateTime = finalFirstEndDateTime;
        this.finalSecondStartDateTime = finalSecondStartDateTime;
        this.finalSecondEndDateTime = finalSecondEndDateTime;
    }

    public ExamPeriod update(ExamPeriodReqDTO dto) {
        this.midFirstStartDateTime = dto.midFirstStartDateTime();
        this.midFirstEndDateTime = dto.midFirstEndDateTime();
        this.midSecondStartDateTime = dto.midSecondStartDateTime();
        this.midSecondEndDateTime = dto.midSecondEndDateTime();
        this.finalFirstStartDateTime = dto.finalFirstStartDateTime();
        this.finalFirstEndDateTime = dto.finalFirstEndDateTime();
        this.finalSecondStartDateTime = dto.finalSecondStartDateTime();
        this.finalSecondEndDateTime = dto.finalSecondEndDateTime();
        return this;
    }

}
