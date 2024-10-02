package com.beehyv.backend.controllers;

import com.beehyv.backend.configurations.filters.JwtFilter;
import com.beehyv.backend.dto.request.*;
import com.beehyv.backend.dto.response.EmployeeResponseDTO;
import com.beehyv.backend.dto.response.TaskResponseDTO;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.models.Attribute;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.services.AdminService;
import com.beehyv.backend.services.EmployeeService;
import com.beehyv.backend.services.userdetails.EmployeeDetailsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
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
        List<EmployeeResponseDTO> employeeResponseDTOS = adminService.findAllEmployees();
        return new ResponseEntity<>(employeeResponseDTOS, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeResponseDTO getEmployee(@PathVariable("employeeId") Integer employeeId){
        return employeeService.getEmployee(employeeId);
    }

    //CREATE EMPLOYEE:
    @PostMapping("/register")
    public EmployeeResponseDTO saveEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO){
        return employeeService.registerEmployee(employeeRequestDTO);
    }
    @GetMapping("/all-designations")
    public ResponseEntity<?> getAllDesignations(){
        return new ResponseEntity<>(adminService.getAllDesignations(), HttpStatus.OK);
    }
    @PostMapping("/all-designations")
    public ResponseEntity<?> saveDesignation(@Valid @RequestBody DesignationRequestDTO designationRequestDTO){
        return new ResponseEntity<>(adminService.saveDesignation(designationRequestDTO), HttpStatus.OK);
    }
    @GetMapping("/all-attributes")
    public ResponseEntity<?> getAllAttributes(){
        return new ResponseEntity<>(adminService.getAllAttributes(), HttpStatus.OK);
    }

    //TASKS:
    @GetMapping("/employee/{employeeId}/tasks")
    public List<TaskResponseDTO> getEmployeeTasks(@PathVariable("employeeId") Integer employeeId){
        return employeeService.getTasks(employeeId);
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
    @PutMapping("/tasks")
    public TaskResponseDTO updateTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return employeeService.updateTask(employeeDetails.getEmployeeId(), taskRequestDTO);
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

    @GetMapping("/appraisal-requests/{appraisalId}")
    public ResponseEntity<?> getAppraisal(@PathVariable("appraisalId") Integer appraisalId){
        return new ResponseEntity<>(adminService.getAppraisal(appraisalId), HttpStatus.OK);
    }

    @PutMapping("/appraisal-requests/tasks")
    public TaskResponseDTO rateTask(@Valid @RequestBody RateTaskRequestDTO rateTaskRequestDTO){
        return adminService.rateTaskByAdmin(rateTaskRequestDTO);
    }

    @PutMapping("/appraisal-requests/{appraisalId}/attributes")
    public String rateEmployeeAttribute(@PathVariable("appraisalId") Integer appraisalId, @Valid @RequestBody RateAttributeRequestDTO rateAttributeRequestDTO) {
        return adminService.rateAttribute(appraisalId, rateAttributeRequestDTO);
    }

    @PutMapping("/appraisal-requests/{appraisalId}")
    public ResponseEntity<?> submitRatingOfAppraisal(@PathVariable("appraisalId") Integer appraisalId){
        return new ResponseEntity<>(adminService.submitRatingOfAppraisal(appraisalId), HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}/appraisals")
    public ResponseEntity<?> getPrevious(@PathVariable("employeeId") Integer employeeId){
        return new ResponseEntity<>(adminService.getPreviousAppraisals(employeeId), HttpStatus.OK);
    }

    //PEOPLE:
    @GetMapping("/people/{name}")
    public ResponseEntity<?> findPeople(@PathVariable("name") @NotEmpty String name){
        try{
            Integer employeeId = Integer.valueOf(name);
            return new ResponseEntity<>(Collections.singletonList(employeeService.getEmployee(employeeId)), HttpStatus.OK);
        }
        catch (NumberFormatException e){
            return new ResponseEntity<>(employeeService.findPeople(name), HttpStatus.OK);
        }

    }
}
