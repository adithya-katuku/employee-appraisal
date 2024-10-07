package com.beehyv.backend.dto.response;

import com.beehyv.backend.models.embeddable.AttributeDAO;

import java.util.List;

public record AppraisalDetailsDTO(
    FullEmployeeResponseDTO employeeResponseDTO,
    List<AttributeDAO> attributes,
    List<TaskResponseDTO> tasks,
    Boolean fullyRated
) {
}
