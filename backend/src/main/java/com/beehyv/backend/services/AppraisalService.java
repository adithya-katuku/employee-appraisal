package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.AppraisalDTOMapper;
import com.beehyv.backend.dto.request.AppraisalRequestDTO;
import com.beehyv.backend.dto.response.AppraisalDTO;
import com.beehyv.backend.dto.response.AppraisalFormEntryDTO;
import com.beehyv.backend.exceptions.InvalidInputException;
import com.beehyv.backend.exceptions.ResourceNotFoundException;
import com.beehyv.backend.models.Appraisal;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Notification;
import com.beehyv.backend.models.embeddable.AttributeDAO;
import com.beehyv.backend.models.enums.AppraisalEligibility;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.repositories.AppraisalRepo;
import com.beehyv.backend.userdetails.EmployeeDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppraisalService {
    @Autowired
    AppraisalRepo appraisalRepo;
    @Autowired
    EmployeeService employeeService;

    public void checkIfEmployeeEligibleForAppraisal(EmployeeDetails employeeDetails) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, -1);
        Integer employeeId = employeeDetails.getEmployeeId();
        Date previousAppraisalDate = employeeDetails.getPreviousAppraisalDate();

        if (previousAppraisalDate.before(calendar.getTime()) && employeeDetails.getAppraisalEligibility() == AppraisalEligibility.NOT_ELIGIBLE) {
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

        List<AttributeDAO> attributeDAOs = employeeService.getAttributes(employeeId).stream()
                        .map(attribute -> new AttributeDAO(attribute.getAttribute(), null))
                        .collect(Collectors.toCollection(ArrayList::new));
        appraisal.setAttributes(attributeDAOs);
        appraisalRepo.save(appraisal);

        return new AppraisalDTOMapper().apply(appraisal);
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
