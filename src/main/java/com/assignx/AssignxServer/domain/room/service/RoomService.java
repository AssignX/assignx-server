package com.assignx.AssignxServer.domain.room.service;

import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.room.dto.AdminRoomCreateReqDTO;
import com.assignx.AssignxServer.domain.room.dto.AdminRoomReqDTO;
import com.assignx.AssignxServer.domain.room.dto.AdminRoomResDTO;
import com.assignx.AssignxServer.domain.room.entity.Room;
import com.assignx.AssignxServer.domain.room.exception.RoomExceptionUtils;
import com.assignx.AssignxServer.domain.room.repository.RoomRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;

    /**
     * 새로운 강의실을 추가합니다. ADMIN 권한으로 호출됩니다.
     *
     * @param dtos 강의실 생성에 필요한 데이터를 담고 있는 모든 {@link AdminRoomCreateReqDTO} 객체.
     * @return 성공적으로 저장된 {@link AdminRoomResDTO} 리스트.
     */
    @Transactional
    public List<AdminRoomResDTO> addRoomByAdmin(Building building, List<AdminRoomCreateReqDTO> dtos) {
        List<AdminRoomResDTO> savedRoomList = new ArrayList<>();

        for (AdminRoomCreateReqDTO dto : dtos) {
            roomRepository.findByBuildingAndRoomNumber(building, dto.roomNumber())
                    .ifPresent(b -> {
                        throw RoomExceptionUtils.RoomAlreadyExists();
                    });
            Room savedRoom = roomRepository.save(dto.toEntity(building));
            savedRoomList.add(AdminRoomResDTO.fromEntity(savedRoom));
        }

        return savedRoomList;
    }

    /**
     * 특정 건물에 속한 모든 강의실 목록을 조회합니다. ADMIN 권한으로 호출됩니다.
     *
     * @param building 강의실 목록을 조회할 대상 건물 엔티티.
     * @return 해당 건물에 속한 모든 강의실 정보를 담은 {@link AdminRoomResDTO} 리스트.
     */
    public List<AdminRoomResDTO> getRoomsByBuilding(Building building) {
        List<Room> rooms = roomRepository.findAllByBuilding(building);
        return rooms.stream().map(AdminRoomResDTO::fromEntity).toList();
    }

    /**
     * 특정 학과에 속한 모든 강의실 목록을 조회합니다. ADMIN 권한으로 호출됩니다.
     *
     * @param department 강의실 목록을 조회할 대상 학과 엔티티.
     * @return 해당 학과에 속한 모든 강의실 정보를 담은 {@link AdminRoomResDTO} 리스트.
     */
    public List<AdminRoomResDTO> getRoomsByDepartment(Department department) {
        List<Room> rooms = roomRepository.findAllByDepartment(department);
        return rooms.stream().map(AdminRoomResDTO::fromEntity).toList();
    }

    /**
     * 강의실 정보를 업데이트합니다. ADMIN 권한으로 호출됩니다.
     *
     * @param dtos 업데이트할 정보를 담고 있는 {@link AdminRoomReqDTO} 객체.
     * @return 업데이트된 {@link AdminRoomResDTO} 객체.
     */
    @Transactional
    public List<AdminRoomResDTO> updateRoom(List<AdminRoomReqDTO> dtos) {
        List<AdminRoomResDTO> rooms = new ArrayList<>();
        for (AdminRoomReqDTO dto : dtos) {
            Room room = roomRepository.findById(dto.roomId()).orElseThrow(RoomExceptionUtils::RoomNotExist);
            room.update(dto);
            rooms.add(AdminRoomResDTO.fromEntity(room));
        }
        return rooms;
    }

    /**
     * 강의실의 학과 정보를 업데이트합니다. ADMIN 권한으로 호출됩니다.
     *
     * @param roomIds    업데이트할 강의실 ID를 담고 있는 리스트.
     * @param department 업데이트할 학과 정보를 담고 있는 객체.
     * @return 업데이트된 {@link AdminRoomResDTO} 객체.
     */
    @Transactional
    public List<AdminRoomResDTO> updateDepartment(List<Long> roomIds, Department department) {
        List<AdminRoomResDTO> rooms = new ArrayList<>();
        for (Long id : roomIds) {
            Room room = roomRepository.findById(id).orElseThrow(RoomExceptionUtils::RoomNotExist);
            room.update(department);
            rooms.add(AdminRoomResDTO.fromEntity(room));
        }
        return rooms;
    }
}
