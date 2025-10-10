package com.assignx.AssignxServer.domain.room.dto;

import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.room.entity.Room;
import jakarta.validation.constraints.NotNull;

public record RoomReqDTO(
        @NotNull
        RoomActionType actionType,
        Long roomId,
        String roomNumber,
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
