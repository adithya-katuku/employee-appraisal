package com.beehyv.backend.controllers;


import com.beehyv.backend.dto.request.LoginRequestDTO;
import com.beehyv.backend.dto.request.RefreshTokenRequestDTO;
import com.beehyv.backend.dto.response.EmployeeResponseDTO;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.services.authentication.LoginService;
import com.beehyv.backend.services.authentication.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AuthenticationController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping("/home")
    public String home(){
        return "Home page";
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        return new ResponseEntity<>(loginService.handleLogin(loginRequestDTO), HttpStatus.OK);
    }

    @GetMapping("/jwt-login")
    public ResponseEntity<?> jwtLogin(){
        EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Map<String, String> res = new HashMap<>();

        res.put("role", employeeDetails.getRoles().stream().max((r1, r2)->r1.ordinal()-r2.ordinal()).get().toString().toLowerCase());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return new ResponseEntity<>(refreshTokenService.refreshToken(refreshTokenRequestDTO), HttpStatus.OK);
    }
}