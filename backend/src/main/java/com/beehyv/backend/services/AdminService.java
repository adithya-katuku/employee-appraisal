package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.EmployeeDTOMapper;
import com.beehyv.backend.dto.modeldtos.EmployeeDTO;
import com.beehyv.backend.models.Attribute;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.models.Task;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private DesignationRepo designationRepo;
    @Autowired
    private AttributeRepo attributeRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    BeeService beeService;

    public List<EmployeeDTO> findAllEmployees() {
        List<Employee> employees = employeeRepo.findAll();
        EmployeeDTOMapper employeeDTOMapper = new EmployeeDTOMapper();
        return employees.stream().map(employeeDTOMapper).toList();
    }

    //ATTRIBUTES:
    public String rateAttribute(Integer employeeId, Integer attributeId, Integer attributeRating) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);

        if(employee!=null){
            List<Attribute> attributes = employee.getDesignation().getAttributes();
            Map<String, Integer> employeeAttributes = employee.getAttributes();
            if(employeeAttributes==null)employeeAttributes = new HashMap<>();
            for(Attribute attribute:attributes){
                if(attribute.getAttributeId()==attributeId){
                    employeeAttributes.put(attribute.getAttribute(), attributeRating);
                }
            }
            employee.setAttributes(employeeAttributes);

            employeeRepo.save(employee);
            return "success";
        }

        return "Employee not found.";
    }

    //TASKS:
    public Task rateTaskByAdmin(Integer taskId, Integer taskRating){
        Task task = taskRepo.findById(taskId).orElse(null);
        if(task!=null){
            task.setAdminRating(taskRating);
            return taskRepo.save(task);
        }

        return null;
    }

    public void searchEmployeesWhoAreEligibleForAppraisal() {
        List<Employee> employeesEligible = employeeRepo.findByEmployeesWhoAreEligibleForAppraisal(Role.EMPLOYEE, AppraisalStatus.PENDING);
        for(Employee employee: employeesEligible){

            Notification notification = new Notification();
            notification.setNotificationTitle("Pending Appraisal");
            notification.setDescription("Employee "+employee.getEmployeeId()+" is eligible for appraisal.");
            notification.setFromId(employee.getEmployeeId());
            notification.setFromId(employee.getEmployeeId());

            employee.setAppraisalStatus(AppraisalStatus.MARKED);
            employeeRepo.save(employee);
            beeService.addNotificationToAdmin(notification);
        }
    }
}
