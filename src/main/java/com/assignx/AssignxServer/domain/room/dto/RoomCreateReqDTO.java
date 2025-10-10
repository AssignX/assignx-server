package com.assignx.AssignxServer.domain.room.dto;

import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.room.entity.Room;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomCreateReqDTO(
        @NotBlank
        String roomNumber,
        @NotNull
        int roomCapacity
) {
    public Room toEntity(Building building) {
        return Room.builder()
                .roomNumber(roomNumber)
                .roomCapacity(roomCapacity)
                .building(building)
                .build();
    }
}
