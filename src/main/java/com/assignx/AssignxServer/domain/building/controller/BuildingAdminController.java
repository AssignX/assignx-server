package com.assignx.AssignxServer.domain.building.controller;

import com.assignx.AssignxServer.domain.building.dto.AdminBuildingCreateReqDTO;
import com.assignx.AssignxServer.domain.building.dto.AdminBuildingReqDTO;
import com.assignx.AssignxServer.domain.building.dto.AdminBuildingResDTO;
import com.assignx.AssignxServer.domain.building.service.BuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/building")
@Tag(name = "Building Admin", description = "건물 관리 API")
public class BuildingAdminController {

    private final BuildingService buildingService;

    @PostMapping()
    @Operation(summary = "건물 추가", description = "새로운 건물과 강의실 정보를 추가합니다.")
    public ResponseEntity<AdminBuildingResDTO> addBuilding(@Valid @RequestBody AdminBuildingCreateReqDTO dto) {
        AdminBuildingResDTO res = buildingService.addBuilding(dto);
        return ResponseEntity.ok(res);
    }

    @PutMapping()
    @Operation(summary = "건물 수정", description = "특정 건물에 대한 정보를 수정합니다.")
    public ResponseEntity<AdminBuildingResDTO> updateBuilding(@Valid @RequestBody AdminBuildingReqDTO dto) {
        AdminBuildingResDTO res = buildingService.updateBuilding(dto);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{buildingId}")
    @Operation(summary = "건물 삭제", description = "특정 건물에 대한 정보를 삭제합니다.")
    public ResponseEntity<Void> deleteBuilding(@PathVariable("buildingId") Long buildingId) {
        buildingService.deleteBuilding(buildingId);
        return ResponseEntity.ok().build();
    }

}
