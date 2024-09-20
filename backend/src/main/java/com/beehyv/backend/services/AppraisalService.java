package com.beehyv.backend.services;

import com.beehyv.backend.models.Appraisal;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.repositories.AppraisalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class AppraisalService {
    @Autowired
    AppraisalRepo appraisalRepo;
    @Autowired
    EmployeeService employeeService;

    public void checkIfEmployeeEligibleForAppraisal(Date previousAppraisalDate, Integer employeeId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -1);
        if(previousAppraisalDate.before(calendar.getTime())){
            addAppraisalEntry(employeeId);
            notifyAdmins(employeeId);
            changePreviousAppraisalDate(employeeId);
        }
    }

    public void addAppraisalEntry(Integer employeeId){
        Appraisal appraisal = new Appraisal();
        appraisal.setAppraisalStatus(AppraisalStatus.PENDING);
        appraisal.setAppraisalYear(Calendar.YEAR);
        appraisal.setEmployeeId(employeeId);

        appraisalRepo.save(appraisal);
    }

    public void notifyAdmins(Integer employeeId){
        Notification notification = new Notification();
        notification.setNotificationTitle("Pending Appraisal");
        notification.setDescription("Employee "+employeeId+" is eligible for appraisal.");
        notification.setFromId(employeeId);

        employeeService.addNotificationToAdmins(notification);
    }

    private void changePreviousAppraisalDate(Integer employeeId) {
        Employee employee = employeeService.findEmployee(employeeId);
        employee.setPreviousAppraisalDate(new Date());
        employeeService.saveEmployee(employee);
    }
}
