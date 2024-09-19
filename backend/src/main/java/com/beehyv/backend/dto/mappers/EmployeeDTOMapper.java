package com.beehyv.backend.dto.mappers;

import com.beehyv.backend.dto.response.EmployeeDTO;
import com.beehyv.backend.models.Employee;

import java.util.function.Function;

public class EmployeeDTOMapper implements Function<Employee, EmployeeDTO> {
    @Override
    public EmployeeDTO apply(Employee employee) {
        return new EmployeeDTO(
                employee.getEmployeeId(),
                employee.getName(),
                employee.getEmail(),
                employee.getJoiningDate(),
                employee.getRoles(),
                employee.getDesignation().getDesignation(),
                employee.getAppraisalStatus()
        );
    }
}
