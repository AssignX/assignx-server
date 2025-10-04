package com.assignx.AssignxServer.domain.building.dto;

import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.room.dto.AdminRoomResDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record BuildingResDTO(
        @NotNull
        Long buildingId,
        @NotNull
        int buildingNumber,
        @NotBlank
        String buildingName,
        @NotNull
        List<AdminRoomResDTO> rooms
) {
    public static BuildingResDTO fromEntity(Building building, List<AdminRoomResDTO> rooms) {
        return BuildingResDTO.builder()
                .buildingId(building.getId())
                .buildingNumber(building.getBuildingNumber())
                .buildingName(building.getBuildingName())
                .rooms(rooms)
                .build();
    }
}
