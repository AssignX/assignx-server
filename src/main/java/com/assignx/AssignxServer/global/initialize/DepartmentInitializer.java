package com.assignx.AssignxServer.global.initialize;

import com.assignx.AssignxServer.domain.department.dto.DepartmentCreateReqDTO;
import com.assignx.AssignxServer.domain.department.service.DepartmentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentInitializer implements CommandLineRunner {
    private final ObjectMapper objectMapper;
    private final DepartmentService departmentService;

    @Override
    public void run(String... args) throws Exception {
        InputStream inputStream = new ClassPathResource("initialize/departments.json").getInputStream();
        List<DepartmentCreateReqDTO> dtos = objectMapper.readValue(inputStream, new TypeReference<>() {
        });

        for (DepartmentCreateReqDTO dto : dtos) {
            departmentService.addDepartment(dto);
        }

    }
}
