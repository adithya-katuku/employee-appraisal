package com.beehyv.backend.controllers;

import com.beehyv.backend.configurations.filters.JwtFilter;
import com.beehyv.backend.dto.request.AppraisalRequestDTO;
import com.beehyv.backend.dto.request.TaskRequestDTO;
import com.beehyv.backend.dto.response.EmployeeResponseDTO;
import com.beehyv.backend.dto.response.TaskResponseDTO;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.models.Attribute;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.services.AdminService;
import com.beehyv.backend.services.EmployeeService;
import com.beehyv.backend.services.userdetails.EmployeeDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeDetailsService employeeDetailsService;
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private AdminService adminService;

    @GetMapping("/info")
    public EmployeeResponseDTO getInfo(){
        EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return employeeService.getEmployee(employeeDetails.getEmployeeId());
    }

    @GetMapping("/all-employees")
    public ResponseEntity<?> getAllEmployees(){
        System.out.println("here");
        List<EmployeeResponseDTO> employeeResponseDTOS = adminService.findAllEmployees();
        return new ResponseEntity<>(employeeResponseDTOS, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeResponseDTO getEmployee(@PathVariable("employeeId") Integer employeeId){
        return employeeService.getEmployee(employeeId);
    }

    //ATTRIBUTES:
    @GetMapping("/employee/{employeeId}/attributes")
    public List<Attribute> getEmployeeAttributes(@PathVariable("employeeId") Integer employeeId){
        return employeeService.getAttributes(employeeId);
    }

    @PutMapping("/employee/{employeeId}/attributes")
    public String rateEmployeeAttribute(@PathVariable("employeeId") Integer employeeId, @RequestParam("attributeId") Integer attributeId, @RequestParam("rating") Double attributeRating){
        return adminService.rateAttribute(employeeId, attributeId, attributeRating);
    }

    //TASKS:
    @GetMapping("/employee/{employeeId}/tasks")
    public List<TaskResponseDTO> getEmployeeTasks(@PathVariable("employeeId") Integer employeeId){
        return employeeService.getTasks(employeeId);
    }

    @PutMapping("/employee/tasks/{taskId}")
    public TaskResponseDTO rateEmployeeTask(@PathVariable("taskId") Integer taskId, @RequestParam("rating") Double taskRating){
        return adminService.rateTaskByAdmin(taskId, taskRating);
    }

    //NOTIFICATIONS:
    @PostMapping("/employee/{employeeId}/notifications")
    public Notification addNotificationToEmployee(@PathVariable("employeeId") Integer employeeId, @RequestBody Notification notification){
        return adminService.addNotification(employeeId, notification);
    }

    //SELF:
    //NOTIFICATIONS:
    @GetMapping("/notifications")
    public List<Notification> getNotifications(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return employeeService.getNotifications(employeeDetails.getEmployeeId());
    }

    @PutMapping("/notifications/{notificationId}")
    public Notification readOrUnreadNotification(@PathVariable("notificationId") Integer notificationId){
        return employeeService.readOrUnreadNotification(notificationId);
    }

    @DeleteMapping("/notifications/{notificationId}")
    public String deleteNotification(@PathVariable("notificationId") Integer notificationId){
        return employeeService.deleteNotification(notificationId);
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
    public List<TaskResponseDTO> getTasks(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return employeeService.getTasks(employeeDetails.getEmployeeId());
    }

    @PostMapping("/tasks")
    public TaskResponseDTO addTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return employeeService.addTask(employeeDetails.getEmployeeId(), taskRequestDTO);
    }

    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(@PathVariable("taskId") Integer taskId){
        return employeeService.deleteTask(taskId);
    }


    //APPRAISAL:
    @PutMapping("/appraisal/{employeeId}")
    public String startAppraisal(@PathVariable("employeeId") Integer employeeId, @Valid @RequestBody AppraisalRequestDTO appraisalRequestDTO){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return adminService.startAppraisal(employeeDetails.getEmployeeId(), employeeId, appraisalRequestDTO);
    }

    @GetMapping("/appraisal-requests")
    public ResponseEntity<?> getPendingAppraisalRequests(){
        return new ResponseEntity<>(adminService.getPendingAppraisalRequests(), HttpStatus.OK);
    }
}
