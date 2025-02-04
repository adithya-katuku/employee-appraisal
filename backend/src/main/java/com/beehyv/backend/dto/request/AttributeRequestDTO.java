package com.beehyv.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AttributeRequestDTO(
        @NotEmpty
        String name,
        @NotNull
        Double rating
) {
}
