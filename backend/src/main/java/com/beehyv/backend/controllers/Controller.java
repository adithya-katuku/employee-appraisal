package com.beehyv.backend.controllers;


import com.beehyv.backend.models.Attribute;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.models.Task;
import com.beehyv.backend.services.BeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    BeeService beeService;

    @GetMapping("/")
    public String home(){
        return "Hello World";
    }

    @PostMapping("/user")
    public Employee saveEmployee(@RequestBody Employee employee){
        return beeService.saveEmployee(employee);
    }

    @GetMapping("/user/{employeeId}")
    public Employee getEmployee(@PathVariable("employeeId") Integer employeeId){
        return beeService.findEmployee(employeeId);
    }

    //ATTRIBUTES:
    @GetMapping("/user/attributes/{designationId}")
    public List<Attribute> getAttributes(@PathVariable("designationId") Integer designationId){
        return beeService.getAttributes(designationId);
    }

    @PutMapping("/user/attributes/{employeeId}")
    public String rateAttribute(@PathVariable("employeeId") Integer employeeId, @RequestParam("attributeId") Integer attributeId, @RequestParam("rating") Integer attributeRating){
        return beeService.rateAttribute(employeeId, attributeId, attributeRating);
    }

    //TASKS:
    @GetMapping("/user/tasks/{designationId}")
    public List<Task> getTasks(@PathVariable("employeeId") Integer employeeId){
        return beeService.getTasks(employeeId);
    }

    @PostMapping("user/tasks/{employeeId}")
    public Task addTask(@PathVariable("employeeId") Integer employeeId, @RequestBody Task task){
        return beeService.addTask(employeeId, task);
    }

    @PutMapping("/user/tasks/employee")
    public Task rateTaskByEmployee(@RequestParam("taskId") Integer taskId, @RequestParam("rating") Integer taskRating){
        return beeService.rateTaskBySelf(taskId, taskRating);
    }

    @PutMapping("/user/tasks/admin")
    public Task rateTaskByAdmin(@RequestParam("taskId") Integer taskId, @RequestParam("rating") Integer taskRating){
        return beeService.rateTaskByAdmin(taskId, taskRating);
    }

    @DeleteMapping("/user/tasks/{taskId}")
    public String deleteTask(@PathVariable("taskId") Integer taskId){
        return beeService.deleteTask(taskId);
    }

    //NOTIFICATIONS:
    @GetMapping("/user/notifications/{employeeId}")
    public List<Notification> getNotifications(@PathVariable("employeeId") Integer employeeId){
        return beeService.getNotifications(employeeId);
    }

    @PostMapping("/user/notifications/{employeeId}")
    public Notification addNotification(@PathVariable("employeeId") Integer employeeId, @RequestBody Notification notification){
        return beeService.addNotification(employeeId, notification);
    }

    @PutMapping("/user/notifications/{notificationId}")
    public Notification readOrUnreadNotification(@PathVariable("notificationId") Integer notificationId){
        return beeService.readOrUnreadNotification(notificationId);
    }

    @DeleteMapping("/user/notifications/{notificationId}")
    public String deleteNotification(@PathVariable("notificationId") Integer notificationId){
        return beeService.deleteNotification(notificationId);
    }
}