package com.beehyv.backend.dto.response;

import com.beehyv.backend.models.enums.AppraisalEligibility;

import java.util.Date;

public record FullEmployeeResponseDTO(
        Integer employeeId,
        String name,
        String email,
        Date joiningDate,
        String designation,
        AppraisalEligibility appraisalEligibility,
        Date previousAppraisalDate
) {
}
