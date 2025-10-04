package com.assignx.AssignxServer.domain.building.entity;

import com.assignx.AssignxServer.domain.building.dto.BuildingReqDTO;
import com.assignx.AssignxServer.domain.room.entity.Room;
import com.assignx.AssignxServer.global.common.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
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
public class Building extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private Long id;

    @Column(nullable = false)
    private int buildingNumber;

    @Column(nullable = false)
    private String buildingName;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

    @Builder
    public Building(int buildingNumber, String buildingName) {
        this.buildingNumber = buildingNumber;
        this.buildingName = buildingName;
    }

    public void update(BuildingReqDTO dto) {
        this.buildingNumber = dto.buildingNumber();
        this.buildingName = dto.buildingName();
    }
}
