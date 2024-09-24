package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.EmployeeResponseDTOMapper;
import com.beehyv.backend.dto.request.AppraisalRequestDTO;
import com.beehyv.backend.dto.response.EmployeeResponseDTO;
import com.beehyv.backend.dto.response.TaskResponseDTO;
import com.beehyv.backend.exceptions.ResourceNotFoundException;
import com.beehyv.backend.models.*;
import com.beehyv.backend.models.enums.AppraisalEligibility;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.repositories.*;
import jakarta.validation.Valid;
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
    @Autowired
    private AppraisalService appraisalService;

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

    //Appraisal:
    public String startAppraisal(Integer adminId, Integer employeeId, AppraisalRequestDTO appraisalRequestDTO) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);

        if(employee==null){
            throw new ResourceNotFoundException("Employee with id "+employeeId+" is  not found.");
        }
//        employee.getAppraisalEligibility()!=AppraisalEligibility.PROCESSING
        if(employee.getPreviousAppraisalDate().compareTo(appraisalRequestDTO.startDate())>=0){
            appraisalService.addAppraisalEntry(adminId, employeeId, appraisalRequestDTO);
            appraisalService.changePreviousAppraisalDateAndEligibility(employeeId, appraisalRequestDTO.endDate(), AppraisalEligibility.PROCESSING);

            Notification notification = new Notification();
            notification.setNotificationTitle("Appraisal!");
            notification.setDescription("Congratulations! You are eligible for an appraisal. Please add your tasks to the appraisal form.");
            notification.setFromId(adminId);

            addNotification(employeeId, notification);

            return "Started appraisal process for the employee with id: "+employeeId;
        }

        return "An appraisal for this employee already exists for the selected period.";
    }
}
