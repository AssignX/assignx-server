package com.assignx.AssignxServer.domain.building.controller;

import com.assignx.AssignxServer.domain.building.dto.BuildingListResDTO;
import com.assignx.AssignxServer.domain.building.dto.BuildingResDTO;
import com.assignx.AssignxServer.domain.building.service.BuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/building")
@Tag(name = "Building", description = "건물 API")
public class BuildingController {

    private final BuildingService buildingService;

    @GetMapping()
    @Operation(summary = "건물 목록 조회", description = "건물 목록을 조회합니다.")
    public ResponseEntity<List<BuildingListResDTO>> getAllBuilding() {
        List<BuildingListResDTO> res = buildingService.getAllBuilding();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{buildingId}")
    @Operation(summary = "건물 상세 조회", description = "특정 건물에 대한 정보를 조회합니다.")
    public ResponseEntity<BuildingResDTO> getBuilding(@PathVariable("buildingId") Long buildingId) {
        BuildingResDTO res = buildingService.getBuilding(buildingId);
        return ResponseEntity.ok(res);
    }

}
