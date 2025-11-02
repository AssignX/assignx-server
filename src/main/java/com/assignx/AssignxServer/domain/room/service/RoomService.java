package com.assignx.AssignxServer.domain.room.service;

import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.room.dto.RoomActionType;
import com.assignx.AssignxServer.domain.room.dto.RoomReqDTO;
import com.assignx.AssignxServer.domain.room.dto.RoomResDTO;
import com.assignx.AssignxServer.domain.room.entity.Room;
import com.assignx.AssignxServer.domain.room.exception.RoomExceptionUtils;
import com.assignx.AssignxServer.domain.room.repository.RoomRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;

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

    /**
     * 강의실 정보를 ActionType 기반으로 일괄 추가/수정/삭제 처리합니다.
     *
     * @param building 대상 건물 Entity.
     * @param roomDtos 업데이트할 정보를 담고 있는 {@link RoomReqDTO} 리스트.
     * @return 처리된 최종 {@link RoomResDTO} 리스트.
     */
    @Transactional
    public List<RoomResDTO> syncRooms(Building building, List<RoomReqDTO> roomDtos) {
        roomDtos.forEach(dto -> {
            if (dto.actionType() == RoomActionType.CREATE) {
                if (dto.roomId() != null || dto.roomNumber() == null || dto.roomNumber().isBlank()) {
                    throw RoomExceptionUtils.RoomReqDTOValidation("roomId and roomNumber");
                }
            } else if (dto.actionType() == RoomActionType.UPDATE || dto.actionType() == RoomActionType.DELETE) {
                if (dto.roomId() == null) {
                    throw RoomExceptionUtils.RoomReqDTOValidation("roomId");
                }
            } else {
                throw RoomExceptionUtils.RoomReqDTOValidation("actionType");
            }
            // ... UPDATE 시 roomNumber/roomCapacity 검사 추가
        });

        // 1. DTO를 작업 타입별로 그룹화
        Map<RoomActionType, List<RoomReqDTO>> groupedDtos = roomDtos.stream()
                .collect(Collectors.groupingBy(RoomReqDTO::actionType));

        List<Room> processedRooms = new ArrayList<>();

        // 2. 삭제 처리 (DELETE)
        List<RoomReqDTO> deleteDtos = groupedDtos.getOrDefault(RoomActionType.DELETE, Collections.emptyList());

        Set<Long> deletedRoomIds = deleteDtos.stream()
                .map(RoomReqDTO::roomId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (!deletedRoomIds.isEmpty()) {
            List<Long> roomIdsToDelete = new ArrayList<>(deletedRoomIds);
            roomRepository.deleteAllByIdInBatch(roomIdsToDelete); // 일괄 삭제
        }

        // 3. 중복 검사 (CREATE, UPDATE 대상 + DB 잔존 항목과의 충돌)
        List<RoomReqDTO> createUpdateDtos = Stream.concat(
                groupedDtos.getOrDefault(RoomActionType.CREATE, Collections.emptyList()).stream(),
                groupedDtos.getOrDefault(RoomActionType.UPDATE, Collections.emptyList()).stream()
        ).toList();

        // 3-1. CREATE/UPDATE 대상의 최종 번호 Set 생성
        Set<String> newOrUpdatedRoomNumbers = createUpdateDtos.stream()
                .map(RoomReqDTO::roomNumber)
                .collect(Collectors.toSet());

        // 3-2. 요청 DTO 목록 자체 내 중복 검사 (CREATE끼리, UPDATE끼리, CREATE/UPDATE 간 충돌 방지)
        if (newOrUpdatedRoomNumbers.size() != createUpdateDtos.size()) {
            List<String> allRequestedNumbers = createUpdateDtos.stream().map(RoomReqDTO::roomNumber).toList();
            Set<String> duplicatedInRequest = allRequestedNumbers.stream()
                    .filter(n -> Collections.frequency(allRequestedNumbers, n) > 1)
                    .collect(Collectors.toSet());
            throw RoomExceptionUtils.RoomAlreadyExists(duplicatedInRequest);
        }

        // 3-3. 기존 강의실 목록과의 충돌 검사
        // 모든 기존 강의실 조회 (UPDATE 전의 상태를 파악하기 위함)
        List<Room> allExistingRooms = roomRepository.findAllByBuilding(building);

        // UPDATE 대상 강의실 ID Set
        Set<Long> updatedRoomIds = groupedDtos.getOrDefault(RoomActionType.UPDATE, Collections.emptyList()).stream()
                .map(RoomReqDTO::roomId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // DB에 남아있을 예정인 강의실 번호 Set (DELETE 대상이 아니고, UPDATE 대상도 아님 = 유지될 강의실)
        Set<String> maintainedRoomNumbers = allExistingRooms.stream()
                // [참조 변수 수정] deleteRoomIds -> deletedRoomIds
                .filter(room -> !deletedRoomIds.contains(room.getId()) && !updatedRoomIds.contains(room.getId()))
                .map(Room::getRoomNumber)
                .collect(Collectors.toSet());

        // 3-4. 최종 충돌 확인: CREATE/UPDATE 번호가 유지될 번호와 겹치는지 확인
        Set<String> conflictedNumbers = newOrUpdatedRoomNumbers.stream()
                .filter(maintainedRoomNumbers::contains)
                .collect(Collectors.toSet());

        if (!conflictedNumbers.isEmpty()) {
            throw RoomExceptionUtils.RoomAlreadyExists(conflictedNumbers);
        }

        // 4. 수정 처리
        List<Room> roomsToUpdate = new ArrayList<>();
        // ID 기준으로 조회하여 엔티티를 찾고 DTO로 업데이트
        for (RoomReqDTO dto : groupedDtos.getOrDefault(RoomActionType.UPDATE, Collections.emptyList())) {
            Room room = roomRepository.findById(dto.roomId()).orElseThrow(RoomExceptionUtils::RoomNotExist);
            room.update(dto);
            roomsToUpdate.add(room);
        }
        roomRepository.saveAll(roomsToUpdate); // 일괄 업데이트
        processedRooms.addAll(roomsToUpdate);

        // 5. 추가 처리
        List<Room> roomsToInsert = groupedDtos.getOrDefault(RoomActionType.CREATE, Collections.emptyList()).stream()
                .map(dto -> dto.toEntity(building))
                .toList();

        List<Room> savedRooms = roomRepository.saveAll(roomsToInsert); // 일괄 저장
        processedRooms.addAll(savedRooms);

        // 6. 결과 반환
        return processedRooms.stream().map(RoomResDTO::fromEntity).toList();
    }
}
