package com.beehyv.backend.dto.response;

import com.beehyv.backend.models.enums.AppraisalEligibility;
import com.beehyv.backend.models.enums.Role;

import java.util.Date;
import java.util.List;

public record EmployeeResponseDTO(
        Integer employeeId,
        String name,
        String email,
        Date joiningDate,
        List<Role> roles,
        String designation,
        AppraisalEligibility appraisalEligibility
) {
}
