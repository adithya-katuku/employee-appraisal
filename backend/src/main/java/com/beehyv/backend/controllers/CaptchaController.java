package com.beehyv.backend.controllers;

import com.beehyv.backend.services.authentication.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    CaptchaService captchaService;

    @GetMapping("/generate-captcha")
    public ResponseEntity<?> generateCaptcha(){
        return new ResponseEntity<>(captchaService.generateCaptcha(), HttpStatus.OK);
    }
}