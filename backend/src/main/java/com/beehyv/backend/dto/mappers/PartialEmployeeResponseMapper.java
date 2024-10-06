package com.beehyv.backend.dto.mappers;

import com.beehyv.backend.dto.response.PartialEmployeeResponseDTO;
import com.beehyv.backend.models.Employee;

import java.util.function.Function;

public class PartialEmployeeResponseMapper implements Function<Employee, PartialEmployeeResponseDTO> {
    @Override
    public PartialEmployeeResponseDTO apply(Employee employee) {
        return new PartialEmployeeResponseDTO(
                employee.getEmployeeId(),
                employee.getName(),
                employee.getEmail(),
                employee.getJoiningDate(),
                employee.getDesignation().getDesignation()
        );
    }
}
