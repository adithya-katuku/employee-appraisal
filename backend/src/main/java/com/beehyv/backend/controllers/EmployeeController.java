package com.beehyv.backend.controllers;

import com.beehyv.backend.configurations.filters.JwtFilter;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.models.Attribute;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.models.Task;
import com.beehyv.backend.services.EmployeeService;
import com.beehyv.backend.services.EmployeeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeDetailsService employeeDetailsService;
    @Autowired
    private JwtFilter jwtFilter;


    @GetMapping("/info")
    public Employee getEmployee(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return employeeService.findEmployee(employeeDetails.getEmployeeId());
    }

    //ATTRIBUTES:
    @GetMapping("/attributes")
    public List<Attribute> getAttributes(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return employeeService.getAttributes(employeeDetails.getEmployeeId());
    }

    //TASKS:
    @GetMapping("/tasks")
    public List<Task> getTasks(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return employeeService.getTasks(employeeDetails.getEmployeeId());
    }

    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task task){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return employeeService.addTask(employeeDetails.getEmployeeId(), task);
    }

    @PutMapping("/tasks/{taskId}")
    public Task rateTask(@PathVariable("taskId") Integer taskId, @RequestParam("rating") Integer taskRating){
        return employeeService.rateTaskBySelf(taskId, taskRating);
    }

    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(@PathVariable("taskId") Integer taskId){
        return employeeService.deleteTask(taskId);
    }

    //NOTIFICATIONS:
    @GetMapping("/notifications")
    public List<Notification> getNotifications(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return employeeService.getNotifications(employeeDetails.getEmployeeId());
    }

    @PostMapping("/notifications")
    public String addNotificationToAdmins(@RequestBody Notification notification){
        return employeeService.addNotificationToAdmin(notification);
    }

    @PutMapping("/notifications/{notificationId}")
    public Notification readOrUnreadNotification(@PathVariable("notificationId") Integer notificationId){
        return employeeService.readOrUnreadNotification(notificationId);
    }

    @DeleteMapping("/notifications/{notificationId}")
    public String deleteNotification(@PathVariable("notificationId") Integer notificationId){
        return employeeService.deleteNotification(notificationId);
    }
}
