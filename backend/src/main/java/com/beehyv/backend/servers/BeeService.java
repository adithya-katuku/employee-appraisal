package com.beehyv.backend.servers;

import com.beehyv.backend.models.*;
import com.beehyv.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BeeService {
    @Autowired
    EmployeeRepo employeeRepo;
    @Autowired
    DesignationRepo designationRepo;
    @Autowired
    AttributeRepo attributeRepo;
    @Autowired
    TaskRepo taskRepo;
    @Autowired
    NotificationRepo notificationRepo;

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
        Employee res = employeeRepo.save(employee);
        for(Task task: tasks)task.setEmployee(res);
        taskRepo.saveAll(tasks);
        for(Notification notification:notifications)notification.setEmployee(res);
        notificationRepo.saveAll(notifications);
        return res;
    }

    public Employee findEmployee(Integer id) {
        return employeeRepo.findById(id).orElse(null);
    }

    public Notification addNotification(Integer id, Notification notification) {
        notification.setEmployee(findEmployee(id));
        return notificationRepo.save(notification);
    }

    public String deleteNotification(Integer notificationId) {
        Notification notification = notificationRepo.findById(notificationId).orElse(null);
        if(notification!=null){
            notificationRepo.delete(notification);
        }
        return "Deleted Successfully";
    }

    public String rateAttribute(Integer id, Integer attributeId, Integer attributeRating) {
        Employee employee = employeeRepo.findById(id).orElse(null);

        if(employee!=null){
            List<Attribute> attributes = employee.getDesignation().getAttributes();
            List<Attribute> newAttributes = new ArrayList<>();
            for(Attribute attribute:attributes){
                if(attribute.getAttributeId()==attributeId){
                    attribute.setRating(attributeRating);
                }
                newAttributes.add(attribute.copy());
            }
            employee.setAttributes(newAttributes);
            employeeRepo.save(employee);
            return "success";
        }

        return "the employee does not exist.";
    }
}
