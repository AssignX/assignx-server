package com.assignx.AssignxServer.domain.building.repository;

import com.assignx.AssignxServer.domain.building.entity.Building;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    Optional<Building> findByBuildingNameAndBuildingNumber(@NotBlank String s, @NotNull int i);
}
