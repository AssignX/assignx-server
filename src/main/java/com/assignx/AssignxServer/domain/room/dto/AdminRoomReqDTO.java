package com.assignx.AssignxServer.domain.room.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminRoomReqDTO(
        @NotNull
        Long roomId,
        @NotBlank
        String roomNumber,
        @NotNull
        int roomCapacity
) {
}
