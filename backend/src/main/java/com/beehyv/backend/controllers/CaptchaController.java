package com.beehyv.backend.controllers;

import com.beehyv.backend.dao.CaptchaDAO;
import com.beehyv.backend.services.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/captcha")
@CrossOrigin(originPatterns = "http://localhost:5173")
public class CaptchaController {
    @Autowired
    CaptchaService captchaService;

    @GetMapping("/generate-captcha")
    public ResponseEntity<?> generateCaptcha() throws IOException {
        return captchaService.generateCaptcha();
    }
}