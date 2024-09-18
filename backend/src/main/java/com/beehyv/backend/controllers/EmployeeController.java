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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class EmployeeController {
    @Autowired
    private BeeService beeService;
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
        return beeService.findEmployee(employeeDetails.getEmployeeId());
    }

    //ATTRIBUTES:
    @GetMapping("/attributes")
    public List<Attribute> getAttributes(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return beeService.getAttributes(employeeDetails.getEmployeeId());
    }

    //TASKS:
    @GetMapping("/tasks")
    public List<Task> getTasks(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return beeService.getTasks(employeeDetails.getEmployeeId());
    }

    @PostMapping("/tasks")
    public Task addTask(@RequestBody Task task){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return beeService.addTask(employeeDetails.getEmployeeId(), task);
    }

    @PutMapping("/tasks/{taskId}")
    public Task rateTask(@PathVariable("taskId") Integer taskId, @RequestParam("rating") Integer taskRating){
        return beeService.rateTaskBySelf(taskId, taskRating);
    }

    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(@PathVariable("taskId") Integer taskId){
        return beeService.deleteTask(taskId);
    }

    //NOTIFICATIONS:
    @GetMapping("/notifications")
    public List<Notification> getNotifications(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return beeService.getNotifications(employeeDetails.getEmployeeId());
    }

    @PostMapping("/notifications")
    public String addNotificationToAdmins(@RequestBody Notification notification){
        return beeService.addNotificationToAdmin(notification);
    }

    @PutMapping("/notifications/{notificationId}")
    public Notification readOrUnreadNotification(@PathVariable("notificationId") Integer notificationId){
        return beeService.readOrUnreadNotification(notificationId);
    }

    @DeleteMapping("/notifications/{notificationId}")
    public String deleteNotification(@PathVariable("notificationId") Integer notificationId){
        return beeService.deleteNotification(notificationId);
    }
}
