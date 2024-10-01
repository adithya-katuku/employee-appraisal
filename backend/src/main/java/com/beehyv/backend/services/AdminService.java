package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.AppraisalDTOMapper;
import com.beehyv.backend.dto.mappers.EmployeeResponseDTOMapper;
import com.beehyv.backend.dto.mappers.TaskResponseDTOMapper;
import com.beehyv.backend.dto.request.AppraisalRequestDTO;
import com.beehyv.backend.dto.request.RateAttributeRequestDTO;
import com.beehyv.backend.dto.request.RateTaskRequestDTO;
import com.beehyv.backend.dto.response.*;
import com.beehyv.backend.exceptions.InvalidInputException;
import com.beehyv.backend.models.embeddable.AttributeDAO;
import com.beehyv.backend.exceptions.ResourceNotFoundException;
import com.beehyv.backend.models.*;
import com.beehyv.backend.models.enums.AppraisalEligibility;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private AttributeRepo attributeRepo;
    @Autowired
    private AppraisalRepo appraisalRepo;
    @Autowired
    private TaskService taskService;
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private AppraisalService appraisalService;

    public List<EmployeeResponseDTO> findAllEmployees() {
        List<Employee> employees = employeeRepo.findByRole(Role.EMPLOYEE);
        if(employees==null){
            throw  new ResourceNotFoundException("No employees found.");
        }
        EmployeeResponseDTOMapper employeeResponseDTOMapper = new EmployeeResponseDTOMapper();
        return employees.stream().map(employeeResponseDTOMapper).toList();
    }

    //ATTRIBUTES:
    public String rateAttribute(Integer appraisalId, RateAttributeRequestDTO rateAttributeRequestDTO) {
        Appraisal appraisal = appraisalRepo.findById(appraisalId).orElse(null);
        if(appraisal==null){
            throw  new ResourceNotFoundException("Appraisal with id "+appraisalId+" is not found.");
        }
        if(appraisal.getAppraisalStatus()!=AppraisalStatus.SUBMITTED){
            throw new InvalidInputException("Appraisal with id "+appraisalId+" is already rated.");
        }
        List<AttributeDAO> attributes = new ArrayList<>();
        for(AttributeDAO attributeDAO: rateAttributeRequestDTO.attributes()){
            attributes.add(new AttributeDAO(attributeDAO.getName(), attributeDAO.getRating()));
        }
        appraisal.setAttributes(attributes);
        appraisalRepo.save(appraisal);

        return "Rated successfully";
    }

    //TASKS:
    public TaskResponseDTO rateTaskByAdmin(RateTaskRequestDTO rateTaskRequestDTO){
        return taskService.rateTaskByAdmin(rateTaskRequestDTO.taskId(), rateTaskRequestDTO.rating());
    }

    //NOTIFICATIONS:
    public Notification addNotification(Integer employeeId, Notification notification) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee!=null){
            notification.setEmployee(employee);
            return notificationRepo.save(notification);
        }

        throw  new ResourceNotFoundException("Employee with id "+employeeId+" is  not found.");
    }

    //Appraisal:
    public String startAppraisal(Integer adminId, Integer employeeId, AppraisalRequestDTO appraisalRequestDTO) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);

        if(employee==null){
            throw new ResourceNotFoundException("Employee with id "+employeeId+" is  not found.");
        }
//        employee.getAppraisalEligibility()!=AppraisalEligibility.PROCESSING
//        employee.getPreviousAppraisalDate().compareTo(appraisalRequestDTO.startDate())>=0
        if(employee.getAppraisalEligibility()!=AppraisalEligibility.PROCESSING){
            Appraisal appraisal = appraisalService.addAppraisalEntry(adminId, employeeId, appraisalRequestDTO);
            appraisalService.changePreviousAppraisalDateAndEligibility(employeeId, appraisalRequestDTO.endDate(), AppraisalEligibility.PROCESSING);
            taskService.addAppraisableTasksToAppraisalForm(employeeId, appraisal);

            Notification notification = new Notification();
            notification.setNotificationTitle("Appraisal!");
            notification.setDescription("Congratulations! You are eligible for an appraisal. Please add your tasks to the appraisal form.");

            addNotification(employeeId, notification);

            return "Started appraisal process for the employee with id: "+employeeId;
        }

        return "An appraisal for this employee ais already under process.";
    }

    public List<AppraisalFormEntryDTO> getPendingAppraisalRequests() {
        return appraisalService.getPendingAppraisalRequests();
    }

    public AppraisalDetailsDTO getAppraisal(Integer appraisalId) {
        Appraisal appraisal = appraisalRepo.findById(appraisalId).orElse(null);
        if(appraisal==null){
            throw  new ResourceNotFoundException("Appraisal with id "+appraisalId+" is not found.");
        }
        if(appraisal.getAppraisalStatus()!=AppraisalStatus.SUBMITTED){
            throw new InvalidInputException("Appraisal with id "+appraisalId+" is already rated.");
        }

        Employee employee = employeeRepo.findById(appraisal.getEmployeeId()).orElse(null);
        if(employee==null){
            throw  new ResourceNotFoundException("Employee with id "+appraisal.getEmployeeId()+" is not found.");
        }

        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTOMapper().apply(employee);
        List<AttributeDAO> attributes = appraisal.getAttributes();
        List<TaskResponseDTO> taskResponseDTOs = appraisal.getTasks().stream()
                .map(task -> new TaskResponseDTOMapper().apply(task))
                .sorted((t1, t2)->t1.taskId()-t2.taskId())
                .toList();

        return new AppraisalDetailsDTO(employeeResponseDTO, attributes, taskResponseDTOs);
    }

    public String submitRatingOfAppraisal(Integer appraisalId) {
        Appraisal appraisal = appraisalRepo.findById(appraisalId).orElse(null);
        if(appraisal==null){
            throw  new ResourceNotFoundException("Appraisal with id "+appraisalId+" is not found.");
        }
        if(appraisal.getAppraisalStatus()!=AppraisalStatus.SUBMITTED){
            throw new InvalidInputException("Appraisal with id "+appraisalId+" is already rated.");
        }
        appraisalService.changePreviousAppraisalDateAndEligibility(appraisal.getEmployeeId(), appraisal.getEndDate(), AppraisalEligibility.NOT_ELIGIBLE);
        appraisal.setAppraisalStatus(AppraisalStatus.APPROVED);

        appraisalRepo.save(appraisal);

        return "Successfully submitted rating.";
    }

    public List<AppraisalDTO> getPreviousAppraisals(Integer employeeId) {
        return appraisalService.getAppraisals(employeeId);
    }
}
