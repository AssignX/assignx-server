package com.assignx.AssignxServer.domain.department.dto;

import com.assignx.AssignxServer.domain.department.entity.Department;
import com.assignx.AssignxServer.domain.member.dto.MemberResDTO;
import com.assignx.AssignxServer.domain.room.dto.AdminRoomResDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record DepartmentResDTO(
        @NotNull
        Long departmentId,
        @NotBlank
        String college,
        @NotBlank
        String major,
        @NotNull
        List<MemberResDTO> employees,
        @NotNull
        List<AdminRoomResDTO> rooms
) {
    public static DepartmentResDTO fromEntity(Department department, List<MemberResDTO> employees,
                                              List<AdminRoomResDTO> rooms) {
        return DepartmentResDTO.builder()
                .departmentId(department.getId())
                .college(department.getCollege())
                .major(department.getMajor())
                .employees(employees)
                .rooms(rooms)
                .build();
    }
}
