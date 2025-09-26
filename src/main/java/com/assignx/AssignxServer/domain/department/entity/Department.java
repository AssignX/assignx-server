package com.assignx.AssignxServer.domain.department.entity;

import com.assignx.AssignxServer.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @Column(nullable = false)
    private String college;

    @Column(nullable = false)
    private String major;

    @OneToMany(mappedBy = "department")
    private List<Member> members;

    @Builder
    public Department(String college, String major) {
        this.college = college;
        this.major = major;
    }


}
