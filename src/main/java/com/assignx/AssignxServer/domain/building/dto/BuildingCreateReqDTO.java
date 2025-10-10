package com.assignx.AssignxServer.domain.building.dto;

import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.room.dto.RoomReqDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BuildingCreateReqDTO(
        @NotNull
        int buildingNumber,
        @NotBlank
        String buildingName,
        @NotNull
        List<RoomReqDTO> rooms
) {
    public Building toEntity() {
        return Building.builder()
                .buildingNumber(buildingNumber)
                .buildingName(buildingName)
                .build();
    }
}
