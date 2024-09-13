package com.beehyv.backend.controllers;


import com.beehyv.backend.models.Attribute;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.servers.BeeService;
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

    @GetMapping("/user/{id}")
    public Employee getEmployee(@PathVariable("id") Integer id){
        return beeService.findEmployee(id);
    }

    @GetMapping("/user/notifications/{id}")
    public List<Notification> getNotifications(@PathVariable("id") Integer id){
        return beeService.findEmployee(id).getNotifications();
    }

    @PostMapping("/user/notifications/{id}")
    public Notification addNotification(@PathVariable("id") Integer id, @RequestBody Notification notification){
        return beeService.addNotification(id, notification);
    }

    @DeleteMapping("/user/notifications/{notificationId}")
    public String deleteNotification(@PathVariable("notificationId") Integer notificationId){
        return beeService.deleteNotification(notificationId);
    }

    @GetMapping("/user/attributes/{id}")
    public List<Attribute> getAttributes(@PathVariable("id") Integer id){
        return beeService.findEmployee(id).getDesignation().getAttributes();
    }

    @PutMapping("/user/attributes/{id}")
    public String rateAttribute(@PathVariable("id") Integer id, @RequestParam("attributeId") Integer attributeId, @RequestParam("rating") Integer attributeRating){
        return beeService.rateAttribute(id, attributeId, attributeRating);
    }


}