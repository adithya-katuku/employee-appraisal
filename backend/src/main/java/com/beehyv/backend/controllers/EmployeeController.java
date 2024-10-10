package com.beehyv.backend.controllers;

import com.beehyv.backend.dto.request.TaskRequestDTO;
import com.beehyv.backend.userdetails.EmployeeDetails;
import com.beehyv.backend.services.AppraisalService;
import com.beehyv.backend.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/info")
    public ResponseEntity<?> getEmployee(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return new ResponseEntity<>(employeeService.getEmployee(employeeDetails.getEmployeeId()), HttpStatus.OK);
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

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<?> rateTask(@PathVariable("taskId") Integer taskId, @RequestParam("rating") Double taskRating){
        return new ResponseEntity<>(employeeService.rateTaskBySelf(taskId, taskRating), HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") Integer taskId){
        return new ResponseEntity<>(employeeService.deleteTask(taskId), HttpStatus.OK);
    }

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

    //APPRAISALS:
    @GetMapping("/appraisals")
    public ResponseEntity<?> getAppraisals(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return new ResponseEntity<>(employeeService.getAppraisals(employeeDetails.getEmployeeId()), HttpStatus.OK);
    }

    @PutMapping("/appraisals/{appraisalId}")
    public ResponseEntity<?> submitAppraisal(@PathVariable("appraisalId") Integer appraisalId){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return new ResponseEntity<>(employeeService.submitAppraisal(appraisalId, employeeDetails.getEmployeeId()), HttpStatus.OK);
    }

    //PEOPLE:
    @GetMapping("/people/{name}")
    public ResponseEntity<?> findPeople(@PathVariable("name") String name){
        return new ResponseEntity<>(employeeService.findPeople(name), HttpStatus.OK);
    }
}
