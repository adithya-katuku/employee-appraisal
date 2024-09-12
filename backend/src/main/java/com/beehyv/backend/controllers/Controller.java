package com.beehyv.backend.controllers;

import com.telsukuntunna.practice.models.Employee;
import com.telsukuntunna.practice.servers.BeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    BeeService beeService;

    @GetMapping("/")
    private String home(){
        return "Hello World";
    }

    @PostMapping("/save")
    private Employee saveEmployee(@RequestBody Employee employee){
        return beeService.saveEmployee(employee);
    }
}