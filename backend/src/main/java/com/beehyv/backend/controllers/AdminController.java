package com.beehyv.backend.controllers;

import com.beehyv.backend.configurations.filters.JwtFilter;
import com.beehyv.backend.dto.response.EmployeeDTO;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.models.Attribute;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.models.Task;
import com.beehyv.backend.services.AdminService;
import com.beehyv.backend.services.EmployeeService;
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
    private EmployeeService beeService;
    @Autowired
    private EmployeeDetailsService employeeDetailsService;
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private AdminService adminService;

    @GetMapping("/info")
    public Employee getInfo(){
        EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return beeService.findEmployee(employeeDetails.getEmployeeId());
    }


    @GetMapping("/all-users")
    public List<EmployeeDTO> getAllEmployees(){
        return adminService.findAllEmployees();
    }

    @GetMapping("/employee/{employeeId}")
    public Employee getEmployee(@PathVariable("employeeId") Integer employeeId){
        return beeService.findEmployee(employeeId);
    }

    //ATTRIBUTES:
    //EMPLOYEES:
    @GetMapping("/employee/{employeeId}/attributes")
    public List<Attribute> getEmployeeAttributes(@PathVariable("employeeId") Integer employeeId){
        return beeService.getAttributes(employeeId);
    }

    @PutMapping("/employee/{employeeId}/attributes")
    public String rateEmployeeAttribute(@PathVariable("employeeId") Integer employeeId, @RequestParam("attributeId") Integer attributeId, @RequestParam("rating") Integer attributeRating){
        return adminService.rateAttribute(employeeId, attributeId, attributeRating);
    }

    //SELF:
    @GetMapping("/attributes")
    public List<Attribute> getAttributes(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return beeService.getAttributes(employeeDetails.getEmployeeId());
    }

    //TASKS:
    //EMPLOYEES:
    @GetMapping("/employee/{employeeId}/tasks")
    public List<Task> getEmployeeTasks(@PathVariable("employeeId") Integer employeeId){
        return beeService.getTasks(employeeId);
    }

    @PutMapping("/employee/tasks/{taskId}")
    public Task rateEmployeeTask(@PathVariable("taskId") Integer taskId, @RequestParam("rating") Integer taskRating){
        return adminService.rateTaskByAdmin(taskId, taskRating);
    }

    //SELF:
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

    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(@PathVariable("taskId") Integer taskId){
        return beeService.deleteTask(taskId);
    }

    //NOTIFICATIONS:
    //EMPLOYEE:
    @PostMapping("/employee/{employeeId}/notifications")
    public Notification addNotificationToEmployee(@PathVariable("employeeId") Integer employeeId, @RequestBody Notification notification){
        return beeService.addNotification(employeeId, notification);
    }

    //SELF:
    @GetMapping("/notifications")
    public List<Notification> getNotifications(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        adminService.searchEmployeesWhoAreEligibleForAppraisal();
        return beeService.getNotifications(employeeDetails.getEmployeeId());
    }

//    @PostMapping("/notifications")
//    public String addNotificationToAdmins(@RequestBody Notification notification){
//        return beeService.addNotificationToAdmin(notification);
//    }

    @PutMapping("/notifications/{notificationId}")
    public Notification readOrUnreadNotification(@PathVariable("notificationId") Integer notificationId){
        return beeService.readOrUnreadNotification(notificationId);
    }

    @DeleteMapping("/notifications/{notificationId}")
    public String deleteNotification(@PathVariable("notificationId") Integer notificationId){
        return beeService.deleteNotification(notificationId);
    }
}
