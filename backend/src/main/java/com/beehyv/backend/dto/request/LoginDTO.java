package com.beehyv.backend.dto.request;

public record LoginDTO(
        String email,
        String password,
        Integer captchaId,
        String captchaAnswer
) {
}
