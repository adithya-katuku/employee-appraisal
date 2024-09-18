package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.EmployeeDTOMapper;
import com.beehyv.backend.dto.modeldtos.EmployeeDTO;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.models.*;
import com.beehyv.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BeeService {
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

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public Employee saveEmployee(Employee employee){
        List<Attribute> attributes = attributeRepo.saveOrFindAll(employee.getDesignation().getAttributes());
        Designation designation = designationRepo.findByDesignation(employee.getDesignation().getDesignation());
        if(designation==null){
            employee.getDesignation().setAttributes(attributes);
            designation = designationRepo.saveOrFind(employee.getDesignation());
        }

        employee.setDesignation(designation);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        return employeeRepo.save(employee);
    }

    public Employee findEmployee(Integer employeeId) {
        return employeeRepo.findById(employeeId).orElse(null);
    }

    public List<EmployeeDTO> findAllEmployees() {
        List<Employee> employees = employeeRepo.findAll();
        EmployeeDTOMapper employeeDTOMapper = new EmployeeDTOMapper();
        return employees.stream().map(employeeDTOMapper).toList();
    }

    //ATTRIBUTES:
    public List<Attribute> getAttributes(Integer employeeId){
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee !=null && employee.getDesignation()!=null){
            return employee.getDesignation().getAttributes();
        }

        return null;
    }

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
    public List<Task> getTasks(Integer employeeId){
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee==null){
            return new ArrayList<>();
        }

        return taskRepo.findByEmployee(employee);
    }

    public Task addTask(Integer employeeId, Task task) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee!=null){
            task.setEmployee(employee);
            return taskRepo.save(task);
        }

        return null;
    }

    public Task rateTaskBySelf(Integer taskId, Integer taskRating){
        Task task = taskRepo.findById(taskId).orElse(null);
        if(task!=null){
            task.setSelfRating(taskRating);
            return taskRepo.save(task);
        }

        return null;
    }

    public Task rateTaskByAdmin(Integer taskId, Integer taskRating){
        Task task = taskRepo.findById(taskId).orElse(null);
        if(task!=null){
            task.setAdminRating(taskRating);
            return taskRepo.save(task);
        }

        return null;
    }

    public String deleteTask(Integer taskId) {
        Task task = taskRepo.findById(taskId).orElse(null);
        if(task!=null){
            taskRepo.delete(task);
            return "success";
        }

        return "Task not found.";
    }

    //NOTIFICATIONS:
    public List<Notification> getNotifications(Integer employeeId) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee==null){
            return new ArrayList<>();
        }
        return notificationRepo.findByEmployee(employee);
    }

    public Notification addNotification(Integer employeeId, Notification notification) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee!=null){
            notification.setEmployee(employee);
            return notificationRepo.save(notification);
        }

        return null;
    }

    public String addNotificationToAdmin(Notification notification) {
        List<Employee> admins = employeeRepo.findByRole(Role.ADMIN);
        if(admins==null){
            return "No admins found";
        }
        for(Employee admin: admins){
            Notification newNotification = new Notification();
            newNotification.setNotificationTitle(notification.getNotificationTitle());
            newNotification.setDescription(notification.getDescription());
            newNotification.setFromId(notification.getFromId());
            newNotification.setEmployee(admin);
            notificationRepo.save(newNotification);
        }

        return "success";
    }

    public Notification readOrUnreadNotification(Integer notificationId) {
        Notification notification = notificationRepo.findById(notificationId).orElse(null);
        if(notification!=null){
            notification.setRead(!notification.isRead());
            return notificationRepo.save(notification);
        }

        return null;
    }

    public String deleteNotification(Integer notificationId) {
        Notification notification = notificationRepo.findById(notificationId).orElse(null);
        if(notification!=null){
            notificationRepo.delete(notification);
            return "success";
        }

        return "Notification not found.";
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
            addNotificationToAdmin(notification);
        }
    }
}
