package com.beehyv.backend.services;

import com.beehyv.backend.dao.CaptchaDAO;
import com.beehyv.backend.dto.mappers.CaptchaDTOMapper;
import com.beehyv.backend.dto.modeldtos.CaptchaDTO;
import com.beehyv.backend.models.Captcha;
import com.beehyv.backend.repositories.CaptchaRepo;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
        config.getProperties().setProperty("kaptcha.textproducer.char.length", "4");
        config.getProperties().setProperty("kaptcha.textproducer.font.names", "Arial, Courier");
        config.getProperties().setProperty("kaptcha.noise.color", "black");
        config.getProperties().setProperty("kaptcha.textproducer.char.string", "0123456789");
        defaultKaptcha.setConfig(config);
    }

    public ResponseEntity<?> generateCaptcha() {
        String text = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(text);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            String encodedImage = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            CaptchaDTO captchaDTO = new CaptchaDTOMapper().apply(saveCaptcha(encodedImage, text));
            outputStream.close();

            return new ResponseEntity<>(captchaDTO, HttpStatusCode.valueOf(200));
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to generate captcha", HttpStatusCode.valueOf(500));
        }

    }

    private Captcha saveCaptcha(String image, String text){
        Captcha captcha = new Captcha();
        captcha.setImage(image);
        captcha.setText(captchaEncoder.encode(text));
        captcha.setExpiry(new Date(System.currentTimeMillis()+1000*60*2));
        return captchaRepo.save(captcha);
    }

    public ResponseEntity<String> verifyCaptcha(CaptchaDAO captchaDAO) {
        Captcha captcha = captchaRepo.findById(captchaDAO.captchaId()).orElse(null);

        if(captcha!=null){
            if(captcha.getExpiry().after(new Date())){
                if(captchaEncoder.matches(captchaDAO.captchaAnswer(), captcha.getText())){
                    return new ResponseEntity<>("Successfully verified captcha", HttpStatusCode.valueOf(200));
                }
            }
        }

        return new ResponseEntity<>("Invalid captcha", HttpStatusCode.valueOf(400));
    }
}
