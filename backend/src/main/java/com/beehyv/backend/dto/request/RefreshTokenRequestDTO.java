package com.beehyv.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequestDTO(
        @NotNull
        Integer refreshTokenId,
        @NotEmpty
        String refreshToken
) {
}
