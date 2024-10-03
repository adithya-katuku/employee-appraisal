package com.beehyv.backend.dto.response;

public record AuthenticationResponseDTO(
        String accessToken,
        String role
) {
}
