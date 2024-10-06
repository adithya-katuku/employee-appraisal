package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.EmployeeResponseDTOMapper;
import com.beehyv.backend.dto.mappers.FullEmployeeDetailsMapper;
import com.beehyv.backend.dto.mappers.PartialEmployeeResponseMapper;
import com.beehyv.backend.dto.request.EmployeeRequestDTO;
import com.beehyv.backend.dto.request.TaskRequestDTO;
import com.beehyv.backend.dto.response.EmployeeResponseDTO;
import com.beehyv.backend.dto.response.FullEmployeeResponseDTO;
import com.beehyv.backend.dto.response.PartialEmployeeResponseDTO;
import com.beehyv.backend.dto.response.TaskResponseDTO;
import com.beehyv.backend.exceptions.InvalidInputException;
import com.beehyv.backend.exceptions.ResourceNotFoundException;
import com.beehyv.backend.models.enums.AppraisalEligibility;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.models.*;
import com.beehyv.backend.repositories.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private DesignationRepo designationRepo;
    @Autowired
    private AttributeRepo attributeRepo;
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private TaskService taskService;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public Employee findEmployee(Integer employeeId) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee==null){
            throw  new ResourceNotFoundException("Employee with id "+employeeId+" is  not found.");
        }

        return employee;
    }

    public FullEmployeeResponseDTO getEmployee(Integer employeeId) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee==null){
            throw  new ResourceNotFoundException("Employee with id "+employeeId+" is  not found.");
        }
        return new FullEmployeeDetailsMapper().apply(employee);
    }

    public void saveEmployee(Employee employee){
        List<Attribute> attributes = attributeRepo.saveOrFindAll(employee.getDesignation().getAttributes());
        Designation designation = designationRepo.findByDesignation(employee.getDesignation().getDesignation());
        if(designation==null){
            employee.getDesignation().setAttributes(attributes);
            designation = designationRepo.saveOrFind(employee.getDesignation());
        }

        employee.setDesignation(designation);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

//        return new EmployeeResponseDTOMapper().apply(employeeRepo.save(employee));
    }


    //ATTRIBUTES:
    public List<Attribute> getAttributes(Integer employeeId){
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee !=null && employee.getDesignation()!=null){
            return employee.getDesignation().getAttributes();
        }

        throw  new ResourceNotFoundException("Employee with id "+employeeId+" is  not found.");
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

//    public Notification addNotification(Integer employeeId, Notification notification) {
//        Employee employee = employeeRepo.findById(employeeId).orElse(null);
//        if(employee!=null){
//            notification.setEmployee(employee);
//            return notificationRepo.save(notification);
//        }
//
//        return null;
//    }

    public String addNotificationToAdmins(Notification notification) {
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

        return "success";
    }

    public Notification readOrUnreadNotification(Integer notificationId, Integer employeeId) {
        Notification notification = notificationRepo.findById(notificationId).orElse(null);

        if(notification!=null){
            if(!Objects.equals(notification.getEmployee().getEmployeeId(), employeeId)){
                throw new InvalidInputException("The notification belongs to a different employee.");
            }
            notification.setRead(!notification.isRead());
            return notificationRepo.save(notification);
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

    //People
    public List<PartialEmployeeResponseDTO> findPeople(String name) {
        return employeeRepo.findByNameContainingIgnoreCase(name).stream()
                .map(employee -> new PartialEmployeeResponseMapper().apply(employee))
                .toList();
    }

}
