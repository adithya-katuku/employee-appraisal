package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.AppraisalDTOMapper;
import com.beehyv.backend.dto.request.AppraisalRequestDTO;
import com.beehyv.backend.dto.response.AppraisalDTO;
import com.beehyv.backend.dto.response.AppraisalFormEntryDTO;
import com.beehyv.backend.exceptions.InvalidInputException;
import com.beehyv.backend.exceptions.ResourceNotFoundException;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.models.Appraisal;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.models.embeddable.AttributeDAO;
import com.beehyv.backend.models.enums.AppraisalEligibility;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.repositories.AppraisalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

            String title = "Pending Appraisal";
            String description = "Employee " + employeeId + " is eligible for appraisal.";
            notifyAdmins(employeeId, title, description);

            changePreviousAppraisalDateAndEligibility(employeeId, previousAppraisalDate, AppraisalEligibility.ELIGIBLE);
        }
    }

    public Appraisal addAppraisalEntry(Integer adminId, Integer employeeId, AppraisalRequestDTO appraisalRequestDTO) {
        Appraisal appraisal = new Appraisal();
        appraisal.setAppraisalStatus(AppraisalStatus.INITIATED);
        appraisal.setEmployeeId(employeeId);
        appraisal.setAdminId(adminId);
        appraisal.setStartDate(appraisalRequestDTO.startDate());
        appraisal.setEndDate(appraisalRequestDTO.endDate());

        return appraisalRepo.save(appraisal);
    }

    public void notifyAdmins(Integer employeeId, String title, String description) {
        Notification notification = new Notification();
        notification.setNotificationTitle(title);
        notification.setDescription(description);
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
        return appraisalRepo.findByEmployeeIdOrderByEndDateDesc(employeeId)
                .stream()
                .map(appraisal -> new AppraisalDTOMapper().apply(appraisal))
                .toList();
    }

    public AppraisalDTO submitAppraisal(Integer appraisalId, Integer employeeId) {
        System.out.println("here");
        Appraisal appraisal = appraisalRepo.findById(appraisalId).orElse(null);
        if(appraisal==null){
            throw  new ResourceNotFoundException("Appraisal with id "+appraisalId+" is  not found.");
        }
        if(!Objects.equals(appraisal.getEmployeeId(), employeeId)){
            throw  new InvalidInputException("Invalid request.");
        }

        appraisal.setAppraisalStatus(AppraisalStatus.SUBMITTED);

        String title = "Appraisal Review";
        String description = "Employee " + employeeId + " has submitted his appraisal form. Please review the same";
        notifyAdmins(employeeId, title, description);

        AppraisalDTO appraisalDTO = new AppraisalDTOMapper().apply(appraisal);

        List<AttributeDAO> attributeDAOs = employeeService.getAttributes(employeeId).stream()
                        .map(attribute -> new AttributeDAO(attribute.getAttribute(), (double) -1))
                        .toList();
        appraisalRepo.save(appraisal);

        return appraisalDTO;
    }

    public List<AppraisalFormEntryDTO> getPendingAppraisalRequests() {
        List<Appraisal> appraisals = appraisalRepo.findByAppraisalStatus(AppraisalStatus.SUBMITTED);

        return appraisals.stream()
                .map(appraisal -> {
                    Employee employee = employeeService.findEmployee(appraisal.getEmployeeId());
                    return new AppraisalFormEntryDTO(appraisal.getId(), appraisal.getEmployeeId(), employee.getName());
                })
                .toList();
    }
}
