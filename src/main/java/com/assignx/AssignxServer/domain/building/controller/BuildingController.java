package com.assignx.AssignxServer.domain.building.controller;

import com.assignx.AssignxServer.domain.building.dto.BuildingCreateReqDTO;
import com.assignx.AssignxServer.domain.building.dto.BuildingListResDTO;
import com.assignx.AssignxServer.domain.building.dto.BuildingReqDTO;
import com.assignx.AssignxServer.domain.building.dto.BuildingResDTO;
import com.assignx.AssignxServer.domain.building.service.BuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/building")
@Tag(name = "Building", description = "건물 API")
public class BuildingController {

    private final BuildingService buildingService;

    @PostMapping("/admin")
    @Operation(summary = "건물 추가", description = "새로운 건물과 강의실 정보를 추가합니다.")
    public ResponseEntity<BuildingResDTO> addBuilding(@Valid @RequestBody BuildingCreateReqDTO dto) {
        BuildingResDTO res = buildingService.addBuilding(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/search")
    @Operation(summary = "건물 상세 조회", description = "건물 번호와 이름으로 건물을 조회합니다.")
    public ResponseEntity<List<BuildingListResDTO>> searchBuilding(@RequestParam(required = false) String name,
                                                                   @RequestParam(required = false) Integer number) {
        List<BuildingListResDTO> res = buildingService.searchBuilding(name, number);
        return ResponseEntity.ok(res);
    }

    @GetMapping()
    @Operation(summary = "건물 목록 조회", description = "건물 목록을 조회합니다.")
    public ResponseEntity<List<BuildingListResDTO>> getAllBuilding() {
        List<BuildingListResDTO> res = buildingService.searchBuilding(null, null);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{buildingId}")
    @Operation(summary = "건물 상세 조회", description = "특정 건물에 대한 정보를 조회합니다.")
    public ResponseEntity<BuildingResDTO> getBuilding(@PathVariable("buildingId") Long buildingId) {
        BuildingResDTO res = buildingService.getBuilding(buildingId);
        return ResponseEntity.ok(res);
    }


    @PutMapping("/admin")
    @Operation(summary = "건물 수정", description = "특정 건물에 대한 정보를 수정합니다.")
    public ResponseEntity<BuildingResDTO> updateBuilding(@Valid @RequestBody BuildingReqDTO dto) {
        BuildingResDTO res = buildingService.updateBuilding(dto);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/admin/{buildingId}")
    @Operation(summary = "건물 삭제", description = "특정 건물에 대한 정보를 삭제합니다.")
    public ResponseEntity<Void> deleteBuilding(@PathVariable("buildingId") Long buildingId) {
        buildingService.deleteBuilding(buildingId);
        return ResponseEntity.ok().build();
    }

}
