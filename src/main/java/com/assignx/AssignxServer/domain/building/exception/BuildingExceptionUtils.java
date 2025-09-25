package com.assignx.AssignxServer.domain.building.exception;

import org.springframework.http.HttpStatus;

public class BuildingExceptionUtils {

    public static BuildingException BuildingAlreadyExists() {
        return new BuildingException(HttpStatus.CONFLICT, "이미 존재하는 건물입니다.");
    }

    public static BuildingException BuildingNotExist() {
        return new BuildingException(HttpStatus.NOT_FOUND, "건물이 존재하지 않습니다.");
    }
}
