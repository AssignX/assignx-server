package com.assignx.AssignxServer.domain.building.exception;

import com.jhssong.errorping.exception.BaseDomainException;
import org.springframework.http.HttpStatus;

public class BuildingException extends BaseDomainException {

  protected BuildingException(HttpStatus status, String message) {
    super(status, message);
  }
}
