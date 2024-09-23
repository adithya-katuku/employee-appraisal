package com.beehyv.backend.dto.mappers;

import com.beehyv.backend.dto.response.EmployeeResponseDTO;
import com.beehyv.backend.models.Employee;

import java.util.function.Function;

public class EmployeeResponseDTOMapper implements Function<Employee, EmployeeResponseDTO> {
    @Override
    public EmployeeResponseDTO apply(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getEmployeeId(),
                employee.getName(),
                employee.getEmail(),
                employee.getJoiningDate(),
                employee.getRoles(),
                employee.getDesignation().getDesignation(),
                employee.getAppraisalEligibility()
        );
    }
}
