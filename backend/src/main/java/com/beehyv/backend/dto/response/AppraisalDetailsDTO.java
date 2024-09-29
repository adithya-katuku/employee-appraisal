package com.beehyv.backend.dto.response;

import com.beehyv.backend.models.embeddable.AttributeDAO;

import java.util.List;

public record AppraisalDetailsDTO(
    EmployeeResponseDTO employeeResponseDTO,
    List<AttributeDAO> attributes,
    List<TaskResponseDTO> tasks
) {
}
