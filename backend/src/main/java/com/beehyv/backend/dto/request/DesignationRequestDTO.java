package com.beehyv.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record DesignationRequestDTO(
        @NotBlank(message = "Designation is required.")
        String name,
        @NotEmpty(message = "At least one attribute is needed.")
        List<@NotBlank(message = "Attribute cannot be blank.") String> attributes
) {
}
