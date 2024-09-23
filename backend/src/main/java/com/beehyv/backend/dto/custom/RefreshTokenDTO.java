package com.beehyv.backend.dto.custom;

public record RefreshTokenDTO(
        Integer refreshTokenId,
        String token
) {
}
