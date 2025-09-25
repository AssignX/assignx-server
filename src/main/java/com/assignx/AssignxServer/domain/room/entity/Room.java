package com.assignx.AssignxServer.domain.room.entity;

import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.room.dto.AdminRoomReqDTO;
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
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String roomNumber;

    private int roomCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

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
}
