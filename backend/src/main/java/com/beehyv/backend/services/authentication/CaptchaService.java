package com.beehyv.backend.services.authentication;

import com.beehyv.backend.dto.custom.CaptchaDTO;
import com.beehyv.backend.dto.mappers.CaptchaDTOMapper;
import com.beehyv.backend.models.Captcha;
import com.beehyv.backend.repositories.CaptchaRepo;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;

@Service
public class CaptchaService {
    @Autowired
    CaptchaRepo captchaRepo;

    private BCryptPasswordEncoder captchaEncoder = new BCryptPasswordEncoder(12);

    private final DefaultKaptcha defaultKaptcha;
    public CaptchaService() {
        this.defaultKaptcha = new DefaultKaptcha();
        Config config =new Config(new Properties());
        config.getProperties().setProperty("kaptcha.image.width", "400");
        config.getProperties().setProperty("kaptcha.image.height", "100");
        config.getProperties().setProperty("kaptcha.textproducer.font.color", "blue");
        config.getProperties().setProperty("kaptcha.textproducer.font.size", "60");
        config.getProperties().setProperty("kaptcha.textproducer.char.length", "6");
        config.getProperties().setProperty("kaptcha.textproducer.font.names", "Arial, Courier");
        config.getProperties().setProperty("kaptcha.noise.color", "black");
        config.getProperties().setProperty("kaptcha.textproducer.char.string",
                                                    "0123456789"+
                                                        "abcdefghijklmnopqrstuvwxyz"+
                                                            "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        defaultKaptcha.setConfig(config);
    }

    public CaptchaDTO generateCaptcha(){
        String text = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(text);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "png", outputStream);
            String encodedImage = Base64.getEncoder().encodeToString(outputStream.toByteArray());

            CaptchaDTO captchaDTO = new CaptchaDTOMapper().apply(saveCaptcha(encodedImage, text));
            outputStream.close();
            return captchaDTO;
        } catch (IOException e) {
            throw new InternalError("Error generating captcha");
        }
    }

    private Captcha saveCaptcha(String image, String text){
        Captcha captcha = new Captcha();
        captcha.setImage(image);
        captcha.setText(captchaEncoder.encode(text));
        captcha.setExpiry(new Date(System.currentTimeMillis()+1000*60*2));
        return captchaRepo.save(captcha);
    }

    public boolean verifyCaptcha(CaptchaDTO captchaDTO) {
        Captcha captcha = captchaRepo.findById(captchaDTO.captchaId()).orElse(null);

        if(captcha!=null){
            if(captcha.getExpiry().after(new Date())){
                return captchaEncoder.matches(captchaDTO.captcha(), captcha.getText());
            }
        }

        return false;
    }
}
