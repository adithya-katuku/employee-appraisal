package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.FullEmployeeDetailsMapper;
import com.beehyv.backend.dto.mappers.PartialEmployeeResponseMapper;
import com.beehyv.backend.dto.request.TaskRequestDTO;
import com.beehyv.backend.dto.response.AppraisalDTO;
import com.beehyv.backend.dto.response.FullEmployeeResponseDTO;
import com.beehyv.backend.dto.response.PartialEmployeeResponseDTO;
import com.beehyv.backend.dto.response.TaskResponseDTO;
import com.beehyv.backend.exceptions.InvalidInputException;
import com.beehyv.backend.exceptions.ResourceNotFoundException;
import com.beehyv.backend.models.enums.AppraisalEligibility;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.models.*;
import com.beehyv.backend.repositories.*;
import com.beehyv.backend.userdetails.EmployeeDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeService {
    @Autowired
    private TaskService taskService;
    @Autowired
    private AppraisalService appraisalService;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private NotificationRepo notificationRepo;

    public FullEmployeeResponseDTO getEmployee(Integer employeeId) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee==null){
            throw  new ResourceNotFoundException("Employee with id "+employeeId+" is  not found.");
        }
        return new FullEmployeeDetailsMapper().apply(employee);
    }

    //TASKS:
    public List<TaskResponseDTO> getTasks(Integer employeeId){
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee==null){
            throw  new ResourceNotFoundException("Employee with id "+employeeId+" is  not found.");
        }
        return taskService.getTasks(employee);
    }

    public TaskResponseDTO addTask(Integer employeeId, TaskRequestDTO taskRequestDTO) {
        return taskService.addTask(employeeId, taskRequestDTO);
    }

    public TaskResponseDTO updateTask(Integer employeeId, TaskRequestDTO taskRequestDTO) {
        return taskService.updateTask(employeeId, taskRequestDTO);
    }

    public TaskResponseDTO rateTaskBySelf(Integer taskId, Double taskRating){
        return taskService.rateTaskBySelf(taskId, taskRating);
    }

    public String deleteTask(Integer taskId) {
        return taskService.deleteTask(taskId);
    }

    //NOTIFICATIONS:
    public List<Notification> getNotifications(Integer employeeId) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee==null){
            throw  new ResourceNotFoundException("Employee with id "+employeeId+" is  not found.");
        }
        return notificationRepo.findByEmployeeOrderByNotificationIdDesc(employee);
    }


    public void addNotificationToAdmins(Notification notification) {
        List<Employee> admins = employeeRepo.findByRole(Role.ADMIN);
        if(admins==null){
            throw  new ResourceNotFoundException("No admins found.");
        }
        for(Employee admin: admins){
            Notification newNotification = new Notification();
            newNotification.setNotificationTitle(notification.getNotificationTitle());
            newNotification.setDescription(notification.getDescription());
            newNotification.setFromId(notification.getFromId());
            newNotification.setEmployee(admin);
            notificationRepo.save(newNotification);
        }
    }

    public String readOrUnreadNotification(Integer notificationId, Integer employeeId) {
        Notification notification = notificationRepo.findById(notificationId).orElse(null);

        if(notification!=null){
            if(!Objects.equals(notification.getEmployee().getEmployeeId(), employeeId)){
                throw new InvalidInputException("The notification belongs to a different employee.");
            }
            notification.setRead(!notification.isRead());
            notificationRepo.save(notification);
            return "Successfully marked notification.";
        }

        throw  new ResourceNotFoundException("Notification  not found.");
    }

    public String deleteNotification(Integer notificationId,  Integer employeeId) {
        Notification notification = notificationRepo.findById(notificationId).orElse(null);
        if(notification!=null){
            if(!Objects.equals(notification.getEmployee().getEmployeeId(), employeeId)){
                throw new InvalidInputException("The notification belongs to a different employee.");
            }
            notificationRepo.delete(notification);
            return "success";
        }

        throw  new ResourceNotFoundException("Notification  not found.");
    }

    //PEOPLE:
    public List<PartialEmployeeResponseDTO> findPeople(String name) {
        return employeeRepo.findByNameContainingIgnoreCase(name).stream()
                .map(employee -> new PartialEmployeeResponseMapper().apply(employee))
                .toList();
    }

    //APPRAISALS:
    public void checkIfEmployeeEligibleForAppraisal(EmployeeDetails employeeDetails) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -1);
        Integer employeeId = employeeDetails.getEmployeeId();
        Date previousAppraisalDate = employeeDetails.getPreviousAppraisalDate();

        if (previousAppraisalDate.before(calendar.getTime()) && employeeDetails.getAppraisalEligibility() == AppraisalEligibility.NOT_ELIGIBLE) {
            String title = "Pending Appraisal";
            String description = "Employee " + employeeId + " is eligible for appraisal.";
            notifyAdmins(employeeId, title, description);

            changePreviousAppraisalDateAndEligibility(employeeId, previousAppraisalDate, AppraisalEligibility.ELIGIBLE);
        }
    }

    public void notifyAdmins(Integer employeeId, String title, String description) {
        Notification notification = new Notification();
        notification.setNotificationTitle(title);
        notification.setDescription(description);
        notification.setFromId(employeeId);

        addNotificationToAdmins(notification);
    }

    public void changePreviousAppraisalDateAndEligibility(Integer employeeId, Date newPreviousAppraisalDate, AppraisalEligibility newEligibility) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee==null){
            throw  new ResourceNotFoundException("Employee with id "+employeeId+" is  not found.");
        }
        employee.setPreviousAppraisalDate(newPreviousAppraisalDate);
        employee.setAppraisalEligibility(newEligibility);

        employeeRepo.save(employee);
    }

    public AppraisalDTO submitAppraisal(Integer appraisalId, Integer employeeId) {
        AppraisalDTO appraisal = appraisalService.submitAppraisal(appraisalId, employeeId);

        String title = "Appraisal Review";
        String description = "Employee " + employeeId + " has submitted his appraisal form. Please review the same";
        notifyAdmins(employeeId, title, description);

        return appraisal;
    }
}
