package com.assignx.AssignxServer.domain.member.entity;

import com.assignx.AssignxServer.domain.courseProfessor.entity.CourseProfessor;
import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.global.common.entity.BaseTimeEntity;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, unique = true)
    private String idNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Department department;

    @OneToMany(mappedBy = "professor")
    private List<CourseProfessor> professors;

    @Builder
    public Member(Long id, String name, Role role, String idNumber) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.idNumber = idNumber;
    }

}
