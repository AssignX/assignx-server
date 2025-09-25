package com.assignx.AssignxServer.domain.room.repository;

import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.room.entity.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Object> findByBuildingAndRoomNumber(Building building, String roomNumber);

    List<Room> findAllByBuilding(Building building);
}
