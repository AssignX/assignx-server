package com.assignx.AssignxServer.domain.room.service;

import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.room.dto.RoomCreateReqDTO;
import com.assignx.AssignxServer.domain.room.dto.RoomReqDTO;
import com.assignx.AssignxServer.domain.room.dto.RoomResDTO;
import com.assignx.AssignxServer.domain.room.entity.Room;
import com.assignx.AssignxServer.domain.room.exception.RoomExceptionUtils;
import com.assignx.AssignxServer.domain.room.repository.RoomRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;

    /**
     * 새로운 강의실을 추가합니다.
     *
     * @param dtos 강의실 생성에 필요한 데이터를 담고 있는 모든 {@link RoomCreateReqDTO} 객체.
     * @return 성공적으로 저장된 {@link RoomResDTO} 리스트.
     */
    @Transactional
    public List<RoomResDTO> addRoom(Building building, List<RoomCreateReqDTO> dtos) {
        // 요청된 강의실 번호 리스트 추출
        Set<String> requestedRoomNumbers = dtos.stream()
                .map(RoomCreateReqDTO::roomNumber)
                .collect(Collectors.toSet());

        // DB에서 요청된 번호 중 이미 존재하는 강의실 조회
        List<Room> existingRooms = roomRepository.findByBuildingAndRoomNumberIn(building, requestedRoomNumbers);

        // 중복된 강의실이 발견되면 일괄적으로 예외 발생
        if (!existingRooms.isEmpty()) {
            Set<String> existingRoomNumbers = existingRooms.stream()
                    .map(Room::getRoomNumber)
                    .collect(Collectors.toSet());

            throw RoomExceptionUtils.RoomAlreadyExists(existingRoomNumbers);
        }

        List<Room> newRooms = dtos.stream()
                .map(dto -> dto.toEntity(building))
                .toList();

        List<Room> savedRooms = roomRepository.saveAll(newRooms);

        return savedRooms.stream()
                .map(RoomResDTO::fromEntity)
                .toList();
    }

    /**
     * 특정 건물에 속한 모든 강의실 목록을 조회합니다.
     *
     * @param building 강의실 목록을 조회할 대상 건물 엔티티.
     * @return 해당 건물에 속한 모든 강의실 정보를 담은 {@link RoomResDTO} 리스트.
     */
    public List<RoomResDTO> getRoomsByBuilding(Building building) {
        List<Room> rooms = roomRepository.findAllByBuilding(building);
        return rooms.stream().map(RoomResDTO::fromEntity).toList();
    }

    /**
     * 특정 학과에 속한 모든 강의실 목록을 조회합니다.
     *
     * @param department 강의실 목록을 조회할 대상 학과 엔티티.
     * @return 해당 학과에 속한 모든 강의실 정보를 담은 {@link RoomResDTO} 리스트.
     */
    public List<RoomResDTO> getRoomsByDepartment(Department department) {
        List<Room> rooms = roomRepository.findAllByDepartment(department);
        return rooms.stream().map(RoomResDTO::fromEntity).toList();
    }

    /**
     * 강의실 정보를 업데이트합니다.
     *
     * @param dtos 업데이트할 정보를 담고 있는 {@link RoomReqDTO} 객체.
     * @return 업데이트된 {@link RoomResDTO} 객체.
     */
    @Transactional
    public List<RoomResDTO> updateRoom(List<RoomReqDTO> dtos) {
        List<RoomResDTO> rooms = new ArrayList<>();
        for (RoomReqDTO dto : dtos) {
            Room room = roomRepository.findById(dto.roomId()).orElseThrow(RoomExceptionUtils::RoomNotExist);
            room.update(dto);
            rooms.add(RoomResDTO.fromEntity(room));
        }
        return rooms;
    }

    /**
     * 강의실의 학과 정보를 업데이트합니다.
     *
     * @param roomIds    업데이트할 강의실 ID를 담고 있는 리스트.
     * @param department 업데이트할 학과 정보를 담고 있는 객체.
     * @return 업데이트된 {@link RoomResDTO} 객체.
     */
    @Transactional
    public List<RoomResDTO> updateDepartment(List<Long> roomIds, Department department) {
        List<RoomResDTO> rooms = new ArrayList<>();
        for (Long id : roomIds) {
            Room room = roomRepository.findById(id).orElseThrow(RoomExceptionUtils::RoomNotExist);
            room.update(department);
            rooms.add(RoomResDTO.fromEntity(room));
        }
        return rooms;
    }
}
