package com.beehyv.backend.dto.modeldtos;

import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.models.enums.Role;

import java.util.Date;
import java.util.List;

public record EmployeeDTO(
        Integer employeeId,
        String name,
        String email,
        Date joiningDate,
        List<Role> roles,
        String designation,
        AppraisalStatus appraisalStatus
) {
}
