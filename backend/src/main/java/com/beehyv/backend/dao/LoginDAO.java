package com.beehyv.backend.dao;

public record LoginDAO(
        String email,
        String password,
        Integer captchaId,
        String captchaAnswer
) {
}
