package com.beehyv.backend.controllers;

import com.beehyv.backend.dto.request.*;
import com.beehyv.backend.userdetails.EmployeeDetails;
import com.beehyv.backend.services.AdminService;
import com.beehyv.backend.services.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminService adminService;

    @GetMapping("/info")
    public ResponseEntity<?> getInfo(){
        EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return new ResponseEntity<>(employeeService.getEmployee(employeeDetails.getEmployeeId()), HttpStatus.OK);
    }

    //CREATE EMPLOYEE:
    @PostMapping("/register")
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO){
        return new ResponseEntity<>(adminService.registerEmployee(employeeRequestDTO), HttpStatus.OK);
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
    public ResponseEntity<?> getEmployeeTasks(@PathVariable("employeeId") Integer employeeId){
        return new ResponseEntity<>(employeeService.getTasks(employeeId), HttpStatus.OK);
    }

    //SELF:
    //NOTIFICATIONS:
    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return new ResponseEntity<>(employeeService.getNotifications(employeeDetails.getEmployeeId()), HttpStatus.OK);
    }

    @PutMapping("/notifications/{notificationId}")
    public ResponseEntity<?> readOrUnreadNotification(@PathVariable("notificationId") Integer notificationId){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return new ResponseEntity<>(employeeService.readOrUnreadNotification(notificationId, employeeDetails.getEmployeeId()), HttpStatus.OK);
    }

    @DeleteMapping("/notifications/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable("notificationId") Integer notificationId){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return new ResponseEntity<>(employeeService.deleteNotification(notificationId, employeeDetails.getEmployeeId()), HttpStatus.OK);
    }

    //TASKS:
    @GetMapping("/tasks")
    public ResponseEntity<?> getTasks(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return new ResponseEntity<>(employeeService.getTasks(employeeDetails.getEmployeeId()), HttpStatus.OK);
    }

    @PostMapping("/tasks")
    public ResponseEntity<?> addTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return new ResponseEntity<>(employeeService.addTask(employeeDetails.getEmployeeId(), taskRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/tasks")
    public ResponseEntity<?> updateTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return new ResponseEntity<>(employeeService.updateTask(employeeDetails.getEmployeeId(), taskRequestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") Integer taskId){
        return new ResponseEntity<>(employeeService.deleteTask(taskId), HttpStatus.OK);
    }

    //APPRAISAL:
    @PutMapping("/appraisal/{employeeId}")
    public ResponseEntity<?> startAppraisal(@PathVariable("employeeId") Integer employeeId, @Valid @RequestBody AppraisalRequestDTO appraisalRequestDTO){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return new ResponseEntity<>(adminService.startAppraisal(employeeDetails.getEmployeeId(), employeeId, appraisalRequestDTO), HttpStatus.OK);
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
    public ResponseEntity<?> rateTask(@Valid @RequestBody RateTaskRequestDTO rateTaskRequestDTO){
        return new ResponseEntity<>(adminService.rateTaskByAdmin(rateTaskRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/appraisal-requests/{appraisalId}/attributes")
    public ResponseEntity<?> rateEmployeeAttribute(@PathVariable("appraisalId") Integer appraisalId, @Valid @RequestBody RateAttributesRequestDTO rateAttributesRequestDTO) {
        return new ResponseEntity<>(adminService.rateAttribute(appraisalId, rateAttributesRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/appraisal-requests/{appraisalId}")
    public ResponseEntity<?> submitRatingOfAppraisal(@PathVariable("appraisalId") Integer appraisalId){
        return new ResponseEntity<>(adminService.submitRatingOfAppraisal(appraisalId), HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}/appraisals")
    public ResponseEntity<?> getPreviousAppraisals(@PathVariable("employeeId") Integer employeeId){
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
            return new ResponseEntity<>(adminService.findPeople(name), HttpStatus.OK);
        }
    }
}
