package com.beehyv.backend.dto.response;

import java.util.Date;

public record PartialEmployeeResponseDTO(
        Integer employeeId,
        String name,
        String email,
        Date joiningDate,
        String designation
) {
}
