package com.assignx.AssignxServer.domain.building.dto;

import com.assignx.AssignxServer.domain.building.entity.Building;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AdminBuildingListResDTO(
        @NotNull
        Long buildingId,
        @NotNull
        int buildingNumber,
        @NotBlank
        String buildingName
) {
    public static AdminBuildingListResDTO fromEntity(Building building) {
        return AdminBuildingListResDTO.builder()
                .buildingId(building.getId())
                .buildingNumber(building.getBuildingNumber())
                .buildingName(building.getBuildingName())
                .build();
    }
}
