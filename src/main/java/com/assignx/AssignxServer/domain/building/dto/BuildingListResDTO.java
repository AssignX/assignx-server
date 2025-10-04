package com.assignx.AssignxServer.domain.building.dto;

import com.assignx.AssignxServer.domain.building.entity.Building;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BuildingListResDTO(
        @NotNull
        Long buildingId,
        @NotNull
        int buildingNumber,
        @NotBlank
        String buildingName
) {
    public static BuildingListResDTO fromEntity(Building building) {
        return BuildingListResDTO.builder()
                .buildingId(building.getId())
                .buildingNumber(building.getBuildingNumber())
                .buildingName(building.getBuildingName())
                .build();
    }
}
