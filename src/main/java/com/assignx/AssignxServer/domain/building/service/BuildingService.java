package com.assignx.AssignxServer.domain.building.service;

import com.assignx.AssignxServer.domain.building.dto.AdminBuildingCreateReqDTO;
import com.assignx.AssignxServer.domain.building.dto.AdminBuildingListResDTO;
import com.assignx.AssignxServer.domain.building.dto.AdminBuildingReqDTO;
import com.assignx.AssignxServer.domain.building.dto.AdminBuildingResDTO;
import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.building.exception.BuildingExceptionUtils;
import com.assignx.AssignxServer.domain.building.repository.BuildingRepository;
import com.assignx.AssignxServer.domain.room.dto.AdminRoomResDTO;
import com.assignx.AssignxServer.domain.room.service.RoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BuildingService {

    private final BuildingRepository buildingRepository;

    private final RoomService roomService;

    /**
     * 새로운 건물을 추가합니다. ADMIN 권한으로 호출됩니다.
     *
     * @param dto 건물 생성에 필요한 데이터를 담고 있는 {@link AdminBuildingCreateReqDTO} 객체.
     * @return 성공적으로 저장된 {@link AdminBuildingResDTO} 객체.
     */
    @Transactional
    public AdminBuildingResDTO addBuilding(AdminBuildingCreateReqDTO dto) {
        // 중복 여부 확인
        buildingRepository.findByBuildingNameAndBuildingNumber(dto.buildingName(), dto.buildingNumber())
                .ifPresent(b -> {
                    throw BuildingExceptionUtils.BuildingAlreadyExists();
                });

        // 건물 생성 및 저장
        Building building = dto.toEntity();
        Building savedBuilding = buildingRepository.save(building);

        // 강의실 생성
        List<AdminRoomResDTO> savedRoomList = roomService.addRoomByAdmin(savedBuilding, dto.rooms());

        return AdminBuildingResDTO.fromEntity(savedBuilding, savedRoomList);
    }

    /**
     * 모든 건물을 조회합니다. ADMIN 권한으로 호출됩니다.
     *
     * @return 모든 {@link AdminBuildingListResDTO} 객체 리스트.
     */
    public List<AdminBuildingListResDTO> getAllBuilding() {
        List<Building> buildingList = buildingRepository.findAll();
        return buildingList.stream().map(AdminBuildingListResDTO::fromEntity).toList();
    }

    /**
     * 특정 ID를 가진 건물을 조회합니다. ADMIN 권한으로 호출됩니다.
     *
     * @param buildingId 조회할 건물의 고유 ID.
     * @return 조회된 {@link AdminBuildingResDTO} 객체.
     */
    public AdminBuildingResDTO getBuilding(Long buildingId) {
        // 건물 조회
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(BuildingExceptionUtils::BuildingNotExist);

        // 건물에 존재하는 강의실 조회
        List<AdminRoomResDTO> adminRoomResDTOS = roomService.getRoomsByBuilding(building);
        return AdminBuildingResDTO.fromEntity(building, adminRoomResDTOS);
    }

    /**
     * 건물의 정보를 업데이트합니다. ADMIN 권한으로 호출됩니다.
     *
     * @param dto 업데이트할 정보를 담고 있는 {@link AdminBuildingReqDTO} 객체.
     * @return 업데이트된 {@link AdminBuildingResDTO} 객체.
     */
    @Transactional
    public AdminBuildingResDTO updateBuilding(AdminBuildingReqDTO dto) {
        // 건물 조회
        Building building = buildingRepository.findById(dto.buildingId())
                .orElseThrow(BuildingExceptionUtils::BuildingNotExist);

        // 건물 수정
        building.update(dto);

        // 강의실 수정
        List<AdminRoomResDTO> updatedRooms = roomService.updateRoomByAdmin(dto.rooms());

        return AdminBuildingResDTO.fromEntity(building, updatedRooms);
    }

    /**
     * 특정 ID를 가진 건물을 삭제합니다. ADMIN 권한으로 호출됩니다.
     *
     * @param buildingId 삭제할 건물의 고유 ID.
     */
    @Transactional
    public void deleteBuilding(Long buildingId) {
        Building buildingToDelete = buildingRepository.findById(buildingId)
                .orElseThrow(BuildingExceptionUtils::BuildingNotExist);

        buildingRepository.delete(buildingToDelete);
    }
}
