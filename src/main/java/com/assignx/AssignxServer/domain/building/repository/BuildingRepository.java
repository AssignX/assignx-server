package com.assignx.AssignxServer.domain.building.repository;

import com.assignx.AssignxServer.domain.building.entity.Building;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    Optional<Building> findByBuildingNameAndBuildingNumber(@NotBlank String s, @NotNull int i);

    List<Building> findByBuildingNameContaining(String buildingName);

    @Query("SELECT b FROM Building b WHERE CAST(b.buildingNumber AS string) LIKE %:partialNumber%")
    List<Building> findByBuildingNumberContaining(@Param("partialNumber") String partialNumber);
}
