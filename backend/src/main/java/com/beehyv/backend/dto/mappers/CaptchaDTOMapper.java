package com.beehyv.backend.dto.mappers;

import com.beehyv.backend.dto.custom.CaptchaDTO;
import com.beehyv.backend.models.Captcha;

import java.util.function.Function;

public class CaptchaDTOMapper implements Function<Captcha, CaptchaDTO> {
    @Override
    public CaptchaDTO apply(Captcha captcha) {
        return new CaptchaDTO(captcha.getCaptchaId(), captcha.getImage());
    }
}
