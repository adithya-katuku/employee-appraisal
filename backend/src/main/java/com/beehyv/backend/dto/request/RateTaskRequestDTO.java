package com.beehyv.backend.dto.request;

import jakarta.validation.constraints.NotNull;

public record RateTaskRequestDTO(
        @NotNull
        Integer taskId,
        @NotNull
        Double rating
) {
}
