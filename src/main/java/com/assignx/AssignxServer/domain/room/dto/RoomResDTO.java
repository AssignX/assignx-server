package com.assignx.AssignxServer.domain.room.dto;

import com.assignx.AssignxServer.domain.room.entity.Room;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RoomResDTO(
        @NotNull
        Long roomId,
        @NotNull
        int buildingNumber,
        @NotBlank
        String buildingName,
        @NotBlank
        String roomNumber,
        @NotNull
        int roomCapacity

) {
    public static RoomResDTO fromEntity(Room room) {
        return RoomResDTO.builder()
                .roomId(room.getId())
                .buildingNumber(room.getBuilding().getBuildingNumber())
                .buildingName(room.getBuilding().getBuildingName())
                .roomNumber(room.getRoomNumber())
                .roomCapacity(room.getRoomCapacity())
                .build();
    }
}
