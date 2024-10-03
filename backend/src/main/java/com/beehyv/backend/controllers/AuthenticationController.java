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
import java.util.HashMap;
import java.util.Map;


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
        Map<String, String> res = new HashMap<>();

        res.put("role", employeeDetails.getRoles().stream().max((r1, r2)->r1.ordinal()-r2.ordinal()).get().toString().toLowerCase());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response){
        return new ResponseEntity<>(authenticationService.refreshToken(refreshToken, response), HttpStatus.OK);
    }

    @PostMapping("/log-out")
    public ResponseEntity<?> logout(HttpServletResponse response){
        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/refresh-token");
        refreshCookie.setDomain("localhost");
        refreshCookie.setMaxAge(0);

        response.addCookie(refreshCookie);

        return new ResponseEntity<>("Logged out", HttpStatus.OK);
    }
}