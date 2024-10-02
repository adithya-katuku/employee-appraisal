package com.beehyv.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record AttributeRequestDTO(
        @NotEmpty
        String attribute
) {
}
