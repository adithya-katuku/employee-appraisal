package com.beehyv.backend.controllers.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

@RestController
@RequestMapping("/captcha")
@CrossOrigin(origins = "http://localhost:5173")
public class CaptchaController {

    private final DefaultKaptcha defaultKaptcha;
    private String text = null;
    public CaptchaController() {
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
        text = defaultKaptcha.createText();
    }

    @GetMapping("/")
    public void getCaptcha(HttpServletResponse response) throws IOException {
        text = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(text);

        response.setContentType("image/jpeg");
        OutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        outputStream.close();
    }

    @PostMapping("/verify-captcha/{captcha}")
    public ResponseEntity<String> verifyCaptcha(@PathVariable("captcha") String captcha) {
        if (text.equals(captcha)) {
            return new ResponseEntity<>("captcha verification successful", HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>("captcha verification failed", HttpStatusCode.valueOf(403));
        }
    }
}