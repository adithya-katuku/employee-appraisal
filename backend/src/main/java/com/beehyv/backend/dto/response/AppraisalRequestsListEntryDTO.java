package com.beehyv.backend.dto.response;

public record AppraisalRequestsListEntryDTO(
        Integer appraisalId,
        Integer employeeId,
        String employeeName
) {
}
