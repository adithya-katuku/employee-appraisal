package com.beehyv.backend.controllers;


import com.beehyv.backend.models.Employee;
import com.beehyv.backend.services.BeeService;
import com.beehyv.backend.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PublicController {
    @Autowired
    private BeeService beeService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String home(){
        return "Home page";
    }

    @PostMapping("/register")
    public Employee saveEmployee(@RequestBody Employee employee){
        return beeService.saveEmployee(employee);
    }

    @PostMapping("/login")
    public String login(@RequestBody Employee employee){
        System.out.println("Employee: "+ employee);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(employee.getEmail(), employee.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(employee.getEmail());
        }
        return "failed";
    }

}