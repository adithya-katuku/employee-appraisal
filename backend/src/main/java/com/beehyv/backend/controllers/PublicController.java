package com.beehyv.backend.controllers;


import com.beehyv.backend.dao.CaptchaDAO;
import com.beehyv.backend.dao.LoginDAO;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.services.BeeService;
import com.beehyv.backend.services.CaptchaService;
import com.beehyv.backend.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PublicController {
    @Autowired
    private BeeService beeService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/")
    public String home(){
        return "Home page";
    }

    @PostMapping("/register")
    public Employee saveEmployee(@RequestBody Employee employee){
        return beeService.saveEmployee(employee);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDAO loginDAO){
        ResponseEntity<?> captchaResponse = captchaService.verifyCaptcha(new CaptchaDAO(loginDAO.captchaId(), loginDAO.captchaAnswer()));
        if(captchaResponse.getStatusCode().value()!=200){
            return captchaResponse;
        }
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDAO.email(), loginDAO.password()));

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(loginDAO.email());
            }
            else {
                return new ResponseEntity<>("Bad credentials", HttpStatusCode.valueOf(401));
            }
        }
        catch(AuthenticationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(401));
        }
    }
}