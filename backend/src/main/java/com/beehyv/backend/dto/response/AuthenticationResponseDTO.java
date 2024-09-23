package com.beehyv.backend.dto.response;

public record AuthenticationResponseDTO(
        String jwtToken,
        Integer refreshTokenId,
        String refreshToken,
        String role
) {
}
