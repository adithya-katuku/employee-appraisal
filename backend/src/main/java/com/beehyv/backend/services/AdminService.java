package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.*;
import com.beehyv.backend.dto.request.*;
import com.beehyv.backend.dto.response.*;
import com.beehyv.backend.exceptions.InvalidInputException;
import com.beehyv.backend.models.embeddable.AttributeDAO;
import com.beehyv.backend.exceptions.ResourceNotFoundException;
import com.beehyv.backend.models.*;
import com.beehyv.backend.models.enums.AppraisalEligibility;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.repositories.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AppraisalService appraisalService;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private DesignationRepo designationRepo;
    @Autowired
    private AttributeRepo attributeRepo;
    @Autowired
    private AppraisalRepo appraisalRepo;
    @Autowired
    private NotificationRepo notificationRepo;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public String registerEmployee(@Valid EmployeeRequestDTO employeeRequestDTO) {
        if(employeeRepo.findByEmail(employeeRequestDTO.email())!=null){
            throw  new InvalidInputException("Email is already in use. Try with a different email.");
        }
        Employee employee = new Employee();
        employee.setEmployeeId(employeeRequestDTO.employeeId());
        employee.setName(employeeRequestDTO.name());
        employee.setEmail(employeeRequestDTO.email());
        employee.setAppraisalEligibility(AppraisalEligibility.NOT_ELIGIBLE);
        employee.setJoiningDate(employeeRequestDTO.joiningDate());
        employee.setPassword(passwordEncoder.encode(employeeRequestDTO.password()));
        employee.setRoles(employeeRequestDTO.roles());
        employee.setPreviousAppraisalDate(employeeRequestDTO.joiningDate());
        Designation designation = designationRepo.findByDesignation(employeeRequestDTO.designation());
        if(designation==null){
            throw  new InvalidInputException("Designation is not saved yet. Kindly create designation with respective attributes first.");
        }

        employee.setDesignation(designation);
        employeeRepo.save(employee);
        return "Successfully registered employee " + employee.getEmployeeId();
    }

    //ATTRIBUTES:
    public String rateAttribute(Integer appraisalId, RateAttributesRequestDTO rateAttributesRequestDTO) {
        Appraisal appraisal = appraisalRepo.findById(appraisalId).orElse(null);
        if (appraisal == null) {
            throw new ResourceNotFoundException("Appraisal with id " + appraisalId + " is not found.");
        }
        if (appraisal.getAppraisalStatus() != AppraisalStatus.UNDER_REVIEW) {
            throw new InvalidInputException("Appraisal with id " + appraisalId + " is already rated.");
        }
        ArrayList<AttributeDAO> attributes = new ArrayList<>();
        for (AttributeRequestDTO attributeRequestDTO : rateAttributesRequestDTO.attributes()) {
            attributes.add(new AttributeDAO(attributeRequestDTO.name(), attributeRequestDTO.rating()));
        }
        appraisal.setAttributes(attributes);
        appraisalRepo.save(appraisal);

        return "Rated successfully";
    }

    //TASKS:
    public TaskResponseDTO rateTaskByAdmin(RateTaskRequestDTO rateTaskRequestDTO) {
        return taskService.rateTaskByAdmin(rateTaskRequestDTO.taskId(), rateTaskRequestDTO.rating());
    }

    //NOTIFICATIONS:
    public void addNotification(Integer employeeId, String title, String description) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee with id " + employeeId + " is  not found.");
        }
        Notification notification = new Notification();
        notification.setNotificationTitle(title);
        notification.setDescription(description);
        notification.setEmployee(employee);
        notificationRepo.save(notification);
    }

    //Appraisal:
    public String startAppraisal(Integer adminId, Integer employeeId, AppraisalRequestDTO appraisalRequestDTO) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);

        if (employee == null) {
            throw new ResourceNotFoundException("Employee with id " + employeeId + " is  not found.");
        }
        if(employee.getRoles().contains(Role.ADMIN)){
            throw new InvalidInputException("An admin cannot be appraised.");
        }
//        employee.getAppraisalEligibility()!=AppraisalEligibility.PROCESSING
//        employee.getPreviousAppraisalDate().compareTo(appraisalRequestDTO.startDate())>=0
        if (employee.getAppraisalEligibility() != AppraisalEligibility.PROCESSING) {
            Appraisal appraisal = appraisalService.addAppraisalEntry(adminId, employeeId, appraisalRequestDTO);
            employeeService.changePreviousAppraisalDateAndEligibility(employeeId, appraisalRequestDTO.endDate(), AppraisalEligibility.PROCESSING);
            taskService.addAppraisableTasksToAppraisalForm(employeeId, appraisal);

            Notification notification = new Notification();
            notification.setNotificationTitle("Appraisal Initiated!");
            notification.setDescription("Congratulations! You are eligible for an appraisal. Please add your tasks to the appraisal form.");
            String title = "Appraisal!";
            String description = "Congratulations! You are eligible for an appraisal. Please add your tasks to the appraisal form.";
            addNotification(employeeId, title, description);

            return "Started appraisal process for the employee with id: " + employeeId;
        }

        return "An appraisal for this employee ais already under process.";
    }

    public List<AppraisalRequestsListEntryDTO> getPendingAppraisalRequests() {
        return appraisalService.getPendingAppraisalRequests();
    }

    public AppraisalDetailsDTO getAppraisal(Integer appraisalId) {
        Appraisal appraisal = appraisalRepo.findById(appraisalId).orElse(null);
        if (appraisal == null) {
            throw new ResourceNotFoundException("Appraisal with id " + appraisalId + " is not found.");
        }
        if (appraisal.getAppraisalStatus() != AppraisalStatus.UNDER_REVIEW) {
            throw new InvalidInputException("Appraisal with id " + appraisalId + " is already rated.");
        }

        Employee employee = employeeRepo.findById(appraisal.getEmployeeId()).orElse(null);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee with id " + appraisal.getEmployeeId() + " is not found.");
        }

        FullEmployeeResponseDTO employeeResponseDTO = new FullEmployeeDetailsMapper().apply(employee);
        List<AttributeDAO> attributes = appraisal.getAttributes();
        List<TaskResponseDTO> taskResponseDTOs = appraisal.getTasks().stream()
                .map(task -> new TaskResponseDTOMapper().apply(task))
                .sorted((t1, t2) -> t1.taskId() - t2.taskId())
                .toList();
        int unrated = 0;
        for(AttributeDAO attributeDAO: attributes){
            if(attributeDAO.getRating()==null){
                unrated++;
            }
        }
        for(TaskResponseDTO taskResponseDTO:taskResponseDTOs){
            if(taskResponseDTO.adminRating()==null){
                unrated++;
            }
        }
        return new AppraisalDetailsDTO(employeeResponseDTO, attributes, taskResponseDTOs, unrated==0);
    }

    public String submitRatingOfAppraisal(Integer appraisalId) {
        Appraisal appraisal = appraisalRepo.findById(appraisalId).orElse(null);
        if (appraisal == null) {
            throw new ResourceNotFoundException("Appraisal with id " + appraisalId + " is not found.");
        }
        if (appraisal.getAppraisalStatus() != AppraisalStatus.UNDER_REVIEW) {
            throw new InvalidInputException("Appraisal with id " + appraisalId + " is already rated.");
        }
        int unrated = 0;
        for(AttributeDAO attributeDAO:appraisal.getAttributes()){
            if(attributeDAO.getRating()==null){
                unrated++;
            }
        }
        for(Task task:appraisal.getTasks()){
            if(task.getAdminRating()==null){
                unrated++;
            }
        }
        if(unrated!=0){
            throw new InvalidInputException("Appraisal with id " + appraisalId + " is not fully rated.");
        }
        employeeService.changePreviousAppraisalDateAndEligibility(appraisal.getEmployeeId(), appraisal.getEndDate(), AppraisalEligibility.NOT_ELIGIBLE);
        appraisal.setAppraisalStatus(AppraisalStatus.APPROVED);

        String title = "Appraisal Rated!";
        String description = "Your appraisal has been rated by admins.";
        addNotification(appraisal.getEmployeeId(), title, description);

        appraisalRepo.save(appraisal);

        return "Successfully submitted rating.";
    }

    public List<AppraisalDTO> getPreviousAppraisals(Integer employeeId) {
        return appraisalService.getAppraisals(employeeId);
    }

    public List<String> getAllAttributes() {
        return attributeRepo.findAll().stream().map(Attribute::getAttribute).toList();
    }

    public List<String> getAllDesignations() {
        return designationRepo.findAll().stream().map(Designation::getDesignation).toList();
    }

    public String saveDesignation(DesignationRequestDTO designationRequestDTO) {
        List<Attribute> attributes = designationRequestDTO.attributes().stream()
                .map(attributeName -> {
                    Attribute attribute = new Attribute();
                    attribute.setAttribute(attributeName);
                    return attribute;
                })
                .toList();

        attributes = attributeRepo.saveOrFindAll(attributes);
        Designation designation = new Designation();
        designation.setDesignation(designationRequestDTO.name());
        designation.setAttributes(attributes);
        designationRepo.save(designation);

        return "Success";
    }

    public List<FullEmployeeResponseDTO> findPeople(String name) {
        return employeeRepo.findByNameContainingIgnoreCase(name).stream()
                .map(employee -> new FullEmployeeDetailsMapper().apply(employee))
                .toList();
    }
}
