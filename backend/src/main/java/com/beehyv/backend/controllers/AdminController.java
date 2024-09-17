package com.beehyv.backend.controllers;

import com.beehyv.backend.configurations.filters.JwtFilter;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.models.Attribute;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.models.Task;
import com.beehyv.backend.services.BeeService;
import com.beehyv.backend.services.EmployeeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    private BeeService beeService;
    @Autowired
    private EmployeeDetailsService employeeDetailsService;
    @Autowired
    private JwtFilter jwtFilter;

    @GetMapping("/info")
    public Employee getEmployee(){
        EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return beeService.findEmployee(employeeDetails.getEmployeeId());
    }

    @GetMapping("/all-users")
    public List<Employee> getAllEmployees(){
        return beeService.findAllEmployees();
    }

    //ATTRIBUTES:
    @GetMapping("/{designationId}/attributes")
    public List<Attribute> getAttributes(@PathVariable("designationId") Integer designationId){
        return beeService.getAttributes(designationId);
    }

    @PutMapping("/{employeeId}/attributes")
    public String rateAttribute(@PathVariable("employeeId") Integer employeeId, @RequestParam("attributeId") Integer attributeId, @RequestParam("rating") Integer attributeRating){
        return beeService.rateAttribute(employeeId, attributeId, attributeRating);
    }

    //TASKS:
    @GetMapping("/{employeeId}/tasks")
    public List<Task> getTasks(@PathVariable("employeeId") Integer employeeId){
        return beeService.getTasks(employeeId);
    }

    @PutMapping("/tasks/{taskId}")
    public Task rateTaskByAdmin(@PathVariable("taskId") Integer taskId, @RequestParam("rating") Integer taskRating){
        return beeService.rateTaskByAdmin(taskId, taskRating);
    }

    //NOTIFICATIONS:
    @PostMapping("/{employeeId}/notifications")
    public Notification addNotification(@PathVariable("employeeId") Integer employeeId, @RequestBody Notification notification){
        return beeService.addNotification(employeeId, notification);
    }
}
