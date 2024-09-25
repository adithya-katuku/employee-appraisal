package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.AppraisalDTOMapper;
import com.beehyv.backend.dto.request.AppraisalRequestDTO;
import com.beehyv.backend.dto.response.AppraisalDTO;
import com.beehyv.backend.models.Appraisal;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.models.enums.AppraisalEligibility;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.repositories.AppraisalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AppraisalService {
    @Autowired
    AppraisalRepo appraisalRepo;
    @Autowired
    EmployeeService employeeService;

    public void checkIfEmployeeEligibleForAppraisal(Date previousAppraisalDate, AppraisalEligibility appraisalEligibility, Integer employeeId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -1);
        if (previousAppraisalDate.before(calendar.getTime()) && appraisalEligibility == AppraisalEligibility.NOT_ELIGIBLE) {
            notifyAdmins(employeeId);
            changePreviousAppraisalDateAndEligibility(employeeId, previousAppraisalDate, AppraisalEligibility.ELIGIBLE);
        }
    }

    public void addAppraisalEntry(Integer adminId, Integer employeeId, AppraisalRequestDTO appraisalRequestDTO) {
        Appraisal appraisal = new Appraisal();
        appraisal.setAppraisalStatus(AppraisalStatus.PENDING);
        appraisal.setEmployeeId(employeeId);
        appraisal.setAdminId(adminId);
        appraisal.setStartDate(appraisalRequestDTO.startDate());
        appraisal.setEndDate(appraisalRequestDTO.endDate());

        appraisalRepo.save(appraisal);
    }

    public void notifyAdmins(Integer employeeId) {
        Notification notification = new Notification();
        notification.setNotificationTitle("Pending Appraisal");
        notification.setDescription("Employee " + employeeId + " is eligible for appraisal.");
        notification.setFromId(employeeId);

        employeeService.addNotificationToAdmins(notification);
    }

    public void changePreviousAppraisalDateAndEligibility(Integer employeeId, Date newPreviousAppraisalDate, AppraisalEligibility newEligibility) {
        Employee employee = employeeService.findEmployee(employeeId);
        employee.setPreviousAppraisalDate(newPreviousAppraisalDate);
        employee.setAppraisalEligibility(newEligibility);

        employeeService.saveEmployee(employee);
    }

    public List<AppraisalDTO> getAppraisals(Integer employeeId) {
        return appraisalRepo.findByEmployeeId(employeeId)
                .stream()
                .map(appraisal -> new AppraisalDTOMapper().apply(appraisal))
                .toList();
    }
}
