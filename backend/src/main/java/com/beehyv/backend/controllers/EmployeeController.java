package com.beehyv.backend.controllers;

import com.beehyv.backend.dto.request.TaskRequestDTO;
import com.beehyv.backend.dto.response.EmployeeResponseDTO;
import com.beehyv.backend.dto.response.TaskResponseDTO;
import com.beehyv.backend.userdetails.EmployeeDetails;
import com.beehyv.backend.models.Attribute;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.services.AppraisalService;
import com.beehyv.backend.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AppraisalService appraisalService;

    @GetMapping("/info")
    public EmployeeResponseDTO getEmployee(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return employeeService.getEmployee(employeeDetails.getEmployeeId());
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

    @PutMapping("/tasks/{taskId}")
    public TaskResponseDTO rateTask(@PathVariable("taskId") Integer taskId, @RequestParam("rating") Double taskRating){
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
        return employeeService.addNotificationToAdmins(notification);
    }

    @PutMapping("/notifications/{notificationId}")
    public Notification readOrUnreadNotification(@PathVariable("notificationId") Integer notificationId){
        return employeeService.readOrUnreadNotification(notificationId);
    }

    @DeleteMapping("/notifications/{notificationId}")
    public String deleteNotification(@PathVariable("notificationId") Integer notificationId){
        return employeeService.deleteNotification(notificationId);
    }

    //APPRAISALS:
    @GetMapping("/appraisals")
    public ResponseEntity<?> getAppraisals(){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return new ResponseEntity<>(appraisalService.getAppraisals(employeeDetails.getEmployeeId()), HttpStatus.OK);
    }

    @PutMapping("/appraisals/{appraisalId}")
    public ResponseEntity<?> submitAppraisal(@PathVariable("appraisalId") Integer appraisalId){
        EmployeeDetails employeeDetails = (EmployeeDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return new ResponseEntity<>(appraisalService.submitAppraisal(appraisalId, employeeDetails.getEmployeeId()), HttpStatus.OK);
    }

    //PEOPLE:
    @GetMapping("/people/{name}")
    public ResponseEntity<?> findPeople(@PathVariable("name") String name){
        return new ResponseEntity<>(employeeService.findPeople(name), HttpStatus.OK);
    }
}
