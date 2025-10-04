package com.assignx.AssignxServer.domain.building.dto;

import com.assignx.AssignxServer.domain.room.dto.AdminRoomReqDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BuildingReqDTO(
        @NotNull
        Long buildingId,
        @NotNull
        int buildingNumber,
        @NotBlank
        String buildingName,
        @NotNull
        List<AdminRoomReqDTO> rooms
) {
}
