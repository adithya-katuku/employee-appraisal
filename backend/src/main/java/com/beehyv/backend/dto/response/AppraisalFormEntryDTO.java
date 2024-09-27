package com.beehyv.backend.dto.response;

public record AppraisalFormEntryDTO(
        Integer appraisalId,
        Integer employeeId,
        String employeeName
) {
}
