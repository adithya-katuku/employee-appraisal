package com.beehyv.backend.servers;

import com.telsukuntunna.practice.models.Attribute;
import com.telsukuntunna.practice.models.Designation;
import com.telsukuntunna.practice.models.Employee;
import com.telsukuntunna.practice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        Employee tempEmployee = employeeRepo.save(employee);
        Designation tempDesignation = designationRepo.save(employee.getDesignation());
        List<Attribute> attributes = attributeRepo.saveAll(employee.getDesignation().getAttributes().stream().map(attribute -> {
            if(attribute.getDesignations()!=null){
                attribute.getDesignations().add(tempDesignation);
            }
            else{
                attribute.setDesignations(Arrays.asList(tempDesignation));
            }
            return attribute;
        }).collect(Collectors.toList()));
        Designation designation = employee.getDesignation();
        designation.setAttributes(attributes);
        List<Employee> employees = designation.getEmployees();
        if (employees == null) employees = Arrays.asList(tempEmployee);
        else employees.add(tempEmployee);
        designation.setEmployees(employees);
        designation = designationRepo.save(designation);
        employee.setDesignation(designation);

        employee.setTasks(taskRepo.saveAll(employee.getTasks().stream().map(task -> {
            task.setEmployee(tempEmployee);
            return task;
        }).collect(Collectors.toList())));
        employee.setNotifications(notificationRepo.saveAll(employee.getNotifications().stream().map(notification -> {
            notification.setEmployee(tempEmployee);
            return notification;
        }).collect(Collectors.toList())));
        return employeeRepo.save(employee);
    }
}
