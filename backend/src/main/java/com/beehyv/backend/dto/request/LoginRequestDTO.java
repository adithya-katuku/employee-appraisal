package com.beehyv.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(
        @NotBlank(message = "Email is required and cannot be empty")
        @Email(message = "Please provide a valid email address")
        String email,

        @NotBlank(message = "Password is required and cannot be empty")
        String password,

        @NotNull(message = "Captcha ID is required")
        Integer captchaId,

        @NotBlank(message = "Captcha answer is required and cannot be empty")
        String captchaAnswer
) {
}
