package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.AppraisalDTOMapper;
import com.beehyv.backend.dto.request.AppraisalRequestDTO;
import com.beehyv.backend.dto.response.AppraisalDTO;
import com.beehyv.backend.dto.response.AppraisalRequestsListEntryDTO;
import com.beehyv.backend.exceptions.InvalidInputException;
import com.beehyv.backend.exceptions.ResourceNotFoundException;
import com.beehyv.backend.models.Appraisal;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.embeddable.AttributeDAO;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.repositories.AppraisalRepo;
import com.beehyv.backend.repositories.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppraisalService {
    @Autowired
    private AppraisalRepo appraisalRepo;
    @Autowired
    private EmployeeRepo employeeRepo;

    public Appraisal addAppraisalEntry(Integer adminId, Integer employeeId, AppraisalRequestDTO appraisalRequestDTO) {
        Appraisal appraisal = new Appraisal();
        appraisal.setAppraisalStatus(AppraisalStatus.INITIATED);
        appraisal.setEmployeeId(employeeId);
        appraisal.setAdminId(adminId);
        appraisal.setStartDate(appraisalRequestDTO.startDate());
        appraisal.setEndDate(appraisalRequestDTO.endDate());

        return appraisalRepo.save(appraisal);
    }

    public List<AppraisalDTO> getAppraisals(Integer employeeId) {
        return appraisalRepo.findByEmployeeIdOrderByEndDateDesc(employeeId)
                .stream()
                .map(appraisal -> new AppraisalDTOMapper().apply(appraisal))
                .toList();
    }

    public List<AppraisalDTO> getPreviousAppraisals(Integer employeeId) {
        return appraisalRepo.findByEmployeeIdOrderByEndDateDesc(employeeId)
                .stream()
                .filter(appraisal -> appraisal.getAppraisalStatus()==AppraisalStatus.APPROVED)
                .map(appraisal -> new AppraisalDTOMapper().apply(appraisal))
                .toList();
    }

    public AppraisalDTO submitAppraisal(Integer appraisalId, Integer employeeId) {
        Appraisal appraisal = appraisalRepo.findById(appraisalId).orElse(null);
        if (appraisal == null) {
            throw new ResourceNotFoundException("Appraisal with id " + appraisalId + " is  not found.");
        }
        if (!Objects.equals(appraisal.getEmployeeId(), employeeId)) {
            throw new InvalidInputException("Invalid request.");
        }

        appraisal.setAppraisalStatus(AppraisalStatus.UNDER_REVIEW);
        Employee employee = employeeRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee with id " + appraisal.getEmployeeId() + " is not found"));
        List<AttributeDAO> attributeDAOs = employee.getDesignation().getAttributes().stream()
                .map(attribute -> new AttributeDAO(attribute.getAttribute(), null))
                .collect(Collectors.toCollection(ArrayList::new));
        appraisal.setAttributes(attributeDAOs);
        appraisalRepo.save(appraisal);

        return new AppraisalDTOMapper().apply(appraisal);
    }

    public List<AppraisalRequestsListEntryDTO> getPendingAppraisalRequests() {
        List<Appraisal> appraisals = appraisalRepo.findByAppraisalStatus(AppraisalStatus.UNDER_REVIEW);

        return appraisals.stream()
                .map(appraisal -> {
                    Employee employee = employeeRepo.findById(appraisal.getEmployeeId()).orElseThrow(() -> new ResourceNotFoundException("Employee with id " + appraisal.getEmployeeId() + " is not found"));
                    return new AppraisalRequestsListEntryDTO(appraisal.getId(), appraisal.getEmployeeId(), employee.getName());
                })
                .toList();
    }
}
