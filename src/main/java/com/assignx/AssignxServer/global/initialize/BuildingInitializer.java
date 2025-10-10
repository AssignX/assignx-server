package com.assignx.AssignxServer.global.initialize;

import com.assignx.AssignxServer.domain.building.dto.BuildingCreateReqDTO;
import com.assignx.AssignxServer.domain.building.entity.Building;
import com.assignx.AssignxServer.domain.building.service.BuildingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BuildingInitializer implements CommandLineRunner {
    private final ObjectMapper objectMapper;
    private final BuildingService buildingService;

    @Override
    public void run(String... args) throws Exception {
        InputStream inputStream = new ClassPathResource("initialize/buildings.json").getInputStream();
        List<BuildingCreateReqDTO> dtos = objectMapper.readValue(inputStream, new TypeReference<>() {
        });

        List<Building> buildingsToSave = new ArrayList<>();

        for (BuildingCreateReqDTO dto : dtos) {
            buildingService.addBuilding(dto);
        }

    }
}
