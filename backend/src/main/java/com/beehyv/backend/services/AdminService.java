package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.EmployeeResponseDTOMapper;
import com.beehyv.backend.dto.response.EmployeeResponseDTO;
import com.beehyv.backend.dto.response.TaskResponseDTO;
import com.beehyv.backend.exceptions.ResourceNotFoundException;
import com.beehyv.backend.models.*;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private AttributeRepo attributeRepo;
    @Autowired
    private AppraisalRepo appraisalRepo;
    @Autowired
    private TaskService taskService;
    @Autowired
    private NotificationRepo notificationRepo;

    public List<EmployeeResponseDTO> findAllEmployees() {
        List<Employee> employees = employeeRepo.findByRole(Role.EMPLOYEE);
        if(employees==null){
            throw  new ResourceNotFoundException("No employees found.");
        }
        EmployeeResponseDTOMapper employeeResponseDTOMapper = new EmployeeResponseDTOMapper();
        return employees.stream().map(employeeResponseDTOMapper).toList();
    }

    //ATTRIBUTES:
    public String rateAttribute(Integer employeeId, Integer attributeId, Double attributeRating) {
        Attribute attribute = attributeRepo.findById(attributeId).orElse(null);
        if(attribute!=null){
            Appraisal appraisal = appraisalRepo.findByEmployeeIdAndAppraisalStatus(employeeId, AppraisalStatus.SUBMITTED);
            Map<String, Double> attributes = appraisal.getAttributes();
            if(attributes==null) attributes = new HashMap<>();
            attributes.put(attribute.getAttribute(), attributeRating);

            return "Rated successfully";
        }

        throw  new ResourceNotFoundException("Employee with id "+employeeId+" is not found.");
    }

    //TASKS:
    public TaskResponseDTO rateTaskByAdmin(Integer taskId, Double taskRating){
        return taskService.rateTaskByAdmin(taskId, taskRating);
    }

    //NOTIFICATIONS:
    public Notification addNotification(Integer employeeId, Notification notification) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee!=null){
            notification.setEmployee(employee);
            return notificationRepo.save(notification);
        }

        throw  new ResourceNotFoundException("Employee with id "+employeeId+" is  not found.");
    }
}
