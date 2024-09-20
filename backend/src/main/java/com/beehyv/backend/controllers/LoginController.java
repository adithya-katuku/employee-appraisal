package com.beehyv.backend.controllers;


import com.beehyv.backend.dto.request.LoginDTO;
import com.beehyv.backend.dto.response.EmployeeResponseDTO;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.services.authentication.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping("/")
    public String home(){
        return "Home page";
    }

    @PostMapping("/register")
    public EmployeeResponseDTO saveEmployee(@RequestBody Employee employee){
        return loginService.saveEmployee(employee);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        Map<String, String> res = loginService.handleLogin(loginDTO);
        if(res.containsKey("jwt")){
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}