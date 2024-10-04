package com.beehyv.backend.controllers;


import com.beehyv.backend.dto.request.LoginRequestDTO;
import com.beehyv.backend.userdetails.EmployeeDetails;
import com.beehyv.backend.services.authentication.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;


@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/home")
    public String home(){
        return "Home page";
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response){
        return new ResponseEntity<>(authenticationService.handleLogin(loginRequestDTO, response), HttpStatus.OK);
    }

    @GetMapping("/jwt-login")
    public ResponseEntity<?> jwtLogin(){
        EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return new ResponseEntity<>(authenticationService.handleJwtLogin(employeeDetails), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response){
        return new ResponseEntity<>(authenticationService.refreshToken(refreshToken, response), HttpStatus.OK);
    }

    @PostMapping("/log-out")
    public ResponseEntity<?> logout(HttpServletResponse response){
        EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        authenticationService.handleLogout(response, employeeDetails.getEmployeeId());

        return new ResponseEntity<>("Logged out", HttpStatus.OK);
    }
}