package com.beehyv.backend.dto.mappers;

import com.beehyv.backend.dto.response.FullEmployeeResponseDTO;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.enums.Role;

import java.util.function.Function;

public class FullEmployeeResponseMapper implements Function<Employee, FullEmployeeResponseDTO> {
    @Override
    public FullEmployeeResponseDTO apply(Employee employee) {
        return new FullEmployeeResponseDTO(
                employee.getEmployeeId(),
                employee.getName(),
                employee.getEmail(),
                employee.getJoiningDate(),
                employee.getDesignation().getDesignation(),
                employee.getRoles().contains(Role.ADMIN)?null:employee.getAppraisalEligibility(),
                employee.getRoles().contains(Role.ADMIN)?null:employee.getPreviousAppraisalDate()
        );
    }
}
