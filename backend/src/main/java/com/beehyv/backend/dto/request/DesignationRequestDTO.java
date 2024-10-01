package com.beehyv.backend.dto.request;

import com.beehyv.backend.models.Attribute;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record DesignationRequestDTO(
        @NotBlank(message = "Designation is required.")
        String name,
        List<Attribute> attributes
) {
}
