package com.assignx.AssignxServer.domain.building.service;

import com.assignx.AssignxServer.domain.building.dto.BuildingCreateReqDTO;
import com.assignx.AssignxServer.domain.building.dto.BuildingListResDTO;
import com.assignx.AssignxServer.domain.building.dto.BuildingReqDTO;
import com.assignx.AssignxServer.domain.building.dto.BuildingResDTO;
import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.building.exception.BuildingExceptionUtils;
import com.assignx.AssignxServer.domain.building.repository.BuildingRepository;
import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.department.exception.DepartmentExceptionUtils;
import com.assignx.AssignxServer.domain.department.repository.DepartmentRepository;
import com.assignx.AssignxServer.domain.room.dto.RoomResDTO;
import com.assignx.AssignxServer.domain.room.entity.Room;
import com.assignx.AssignxServer.domain.room.service.RoomService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final DepartmentRepository departmentRepository;

    private final RoomService roomService;

    /**
     * 새로운 건물을 추가합니다.
     *
     * @param dto 건물 생성에 필요한 데이터를 담고 있는 {@link BuildingCreateReqDTO} 객체.
     * @return 성공적으로 저장된 {@link BuildingResDTO} 객체.
     */
    @Transactional
    public BuildingResDTO addBuilding(BuildingCreateReqDTO dto) {
        // 중복 여부 확인
        buildingRepository.findByBuildingNameAndBuildingNumber(dto.buildingName(), dto.buildingNumber())
                .ifPresent(b -> {
                    throw BuildingExceptionUtils.BuildingAlreadyExists();
                });

        // 건물 생성 및 저장
        Building building = dto.toEntity();
        Building savedBuilding = buildingRepository.save(building);

        // 강의실 생성
        List<RoomResDTO> savedRoomList = new ArrayList<>();
        if (dto.rooms() != null && !dto.rooms().isEmpty()) {
            System.out.println(dto.rooms());
            savedRoomList = roomService.syncRooms(savedBuilding, dto.rooms());
        }

        return BuildingResDTO.fromEntity(savedBuilding, savedRoomList);
    }

    /**
     * 조건에 맞는 건물 목록을 조회합니다.
     *
     * @param name   조회할 건물 이름
     * @param number 조회할 건물 번호
     * @return 조회된 모든 {@link BuildingListResDTO} 객체 리스트.
     */
    public List<BuildingListResDTO> searchBuilding(String name, Integer number) {
        List<Building> buildings;

        // 이름으로 조회한 경우
        if (name != null && !name.isBlank()) {
            buildings = buildingRepository.findByBuildingName(name);
        }
        // 번호로 조회한 경우
        else if (number != null) {
            buildings = buildingRepository.findByBuildingNumber(number);
        }
        // 조건이 없는 경우 빈 리스트 반환
        else {
            buildings = new ArrayList<>();
        }
        return buildings.stream().map(BuildingListResDTO::fromEntity).toList();
    }

    /**
     * 모든 건물을 조회합니다.
     *
     * @return 조회된 모든 {@link BuildingListResDTO} 객체 리스트.
     */
    public List<BuildingListResDTO> getAllBuildings() {
        List<Building> buildings = buildingRepository.findAll();
        return buildings.stream().map(BuildingListResDTO::fromEntity).toList();
    }

    /**
     * 특정 ID를 가진 건물을 조회합니다.
     *
     * @param buildingId 조회할 건물의 고유 ID.
     * @return 조회된 {@link BuildingResDTO} 객체.
     */
    public BuildingResDTO getBuildingById(Long buildingId) {
        // 건물 조회
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(BuildingExceptionUtils::BuildingNotExist);

        // 건물에 존재하는 강의실 조회
        List<RoomResDTO> roomResDTOS = roomService.getRoomsByBuilding(building);
        return BuildingResDTO.fromEntity(building, roomResDTOS);
    }

    /**
     * 특정 학과 소속의 건물 목록을 조회합니다.
     *
     * @param departmentId 조회할 학과의 고유 ID.
     * @return 조회된 모든 {@link RoomResDTO} 객체 리스트.
     */
    public List<RoomResDTO> getAllBuildingsByDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(DepartmentExceptionUtils::DepartmentNotExist);
        return roomService.getRoomsByDepartment(department);
    }

    /**
     * 건물의 정보를 업데이트합니다.
     *
     * @param dto 업데이트할 정보를 담고 있는 {@link BuildingReqDTO} 객체.
     * @return 업데이트된 {@link BuildingResDTO} 객체.
     */
    @Transactional
    public BuildingResDTO updateBuilding(BuildingReqDTO dto) {
        // 건물 조회
        Building building = buildingRepository.findById(dto.buildingId())
                .orElseThrow(BuildingExceptionUtils::BuildingNotExist);

        // 건물 수정
        building.update(dto);

        // 강의실 수정
        List<RoomResDTO> updatedRooms = roomService.syncRooms(building, dto.rooms());

        return BuildingResDTO.fromEntity(building, updatedRooms);
    }

    /**
     * 특정 ID를 가진 건물을 삭제합니다.
     *
     * @param buildingId 삭제할 건물의 고유 ID.
     */
    @Transactional
    public void deleteBuilding(Long buildingId) {
        Building buildingToDelete = buildingRepository.findById(buildingId)
                .orElseThrow(BuildingExceptionUtils::BuildingNotExist);

        buildingRepository.delete(buildingToDelete);
    }

    /**
     * SY에서 조회한 건물명과 강의실을 실제 강의실 객체와 매핑합니다.
     *
     * @param lctrmInfo 조회할 건물 문자열 (e.g. "산격동 캠퍼스  IT대학5호관(IT융복합관)")
     * @param rmnmCd    조회할 강의실 문자열 (e.g. "342")
     * @return 조회된 강의실 객체 {@link Room}.
     */
    public Room getBuildingFromSYResponse(Object lctrmInfo, Object rmnmCd) {
        String buildingName = lctrmInfo.toString();
        String roomNumber = rmnmCd.toString();

        String buildingSearchStr = buildingName.split(" ")[buildingName.split(" ").length - 1];
        List<Building> searchedBuildings = buildingRepository.findByBuildingNameContaining(buildingSearchStr);
        if (searchedBuildings.size() != 1) {
            // 검색된 건물이 하나가 아니라면 매핑할 수 없음
            // FIXME 컴퓨터학부에서 개설되는 수업의 모든 강의실 정보는 제외한 케이스는 예외처리.
//            throw BuildingExceptionUtils.BuildingParsingFailed(buildingName);
            return null;
        }
        Building building = searchedBuildings.get(0);

        for (Room room : building.getRooms()) {
            if (room.getRoomNumber().equals(roomNumber)) {
                return room;
            }
        }

        // 강의실 객체를 찾지 못한 경우
        // FIXME 컴퓨터학부에서 개설되는 수업의 모든 강의실 정보는 제외한 케이스는 예외처리.
//        throw RoomExceptionUtils.RoomNotExist(roomNumber);
        return null;
    }
}
