package com.beehyv.backend.services;

import com.beehyv.backend.enums.Role;
import com.beehyv.backend.models.*;
import com.beehyv.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        List<Notification> notifications = notificationRepo.saveAll(employee.getNotifications());
        List<Task> tasks = taskRepo.saveAll(employee.getTasks());
        Designation designation = designationRepo.findByDesignation(employee.getDesignation().getDesignation());
        if(designation==null){
            employee.getDesignation().setAttributes(attributes);
            designation = designationRepo.saveOrFind(employee.getDesignation());
        }

        employee.setDesignation(designation);
        employee.setNotifications(notifications);
        employee.setTasks(tasks);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        Employee res = employeeRepo.save(employee);
        for(Task task: tasks)task.setEmployee(res);
        taskRepo.saveAll(tasks);
        for(Notification notification:notifications)notification.setEmployee(res);
        notificationRepo.saveAll(notifications);

        return res;
    }

    public Employee findEmployee(Integer employeeId) {
        return employeeRepo.findById(employeeId).orElse(null);
    }

    public List<Employee> findAllEmployees() {
        return employeeRepo.findAll();
    }

    //ATTRIBUTES:
    public List<Attribute> getAttributes(Integer designationId){
        Designation designation = designationRepo.findById(designationId).orElse(null);
        if(designation!=null){
            return designation.getAttributes();
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
        if(employee!=null){
            return employee.getTasks();
        }

        return null;
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
        if(employee!=null){
            return employee.getNotifications();
        }

        return null;
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
            notification.setEmployee(admin);
            admin.getNotifications().add(notification);
            employeeRepo.save(admin);
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



}
