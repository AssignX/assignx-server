package com.assignx.AssignxServer.domain.room.entity;

import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.room.dto.AdminRoomReqDTO;
import com.assignx.AssignxServer.global.common.entity.BaseTimeEntity;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private int roomCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Department department;

    @Builder
    public Room(String roomNumber, int roomCapacity, Building building) {
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.building = building;
    }

    public void update(AdminRoomReqDTO dto) {
        this.roomNumber = dto.roomNumber();
        this.roomCapacity = dto.roomCapacity();
    }

    public void update(Department department) {
        this.department = department;
    }
}
