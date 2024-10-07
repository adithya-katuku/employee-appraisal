package com.beehyv.backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RateAttributesRequestDTO(
        @NotEmpty
        List<@Valid AttributeRequestDTO> attributes
) {
}
