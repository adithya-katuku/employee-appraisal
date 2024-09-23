package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.TaskResponseDTOMapper;
import com.beehyv.backend.dto.request.TaskRequestDTO;
import com.beehyv.backend.dto.response.TaskResponseDTO;
import com.beehyv.backend.exceptions.ResourceNotFoundException;
import com.beehyv.backend.models.Appraisal;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Task;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private DesignationRepo designationRepo;
    @Autowired
    private AttributeRepo attributeRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private AppraisalRepo appraisalRepo;

    public List<TaskResponseDTO> getTasks(Employee employee){
        List<Task> tasks = taskRepo.findByEmployee(employee);
        if(tasks==null){
            throw  new ResourceNotFoundException("No tasks found for employee with id: "+employee.getEmployeeId()+".");
        }
        return tasks.stream().map(task -> new TaskResponseDTOMapper().apply(task)).toList();
    }

    public TaskResponseDTO addTask(Integer employeeId, TaskRequestDTO taskRequestDTO) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee!=null){
            Task task = new Task();
            if(taskRequestDTO.taskId()!=null){
                task.setTaskId(taskRequestDTO.taskId());
            }
            task.setEmployee(employee);
            task.setTaskTitle(taskRequestDTO.taskTitle());
            task.setDescription(taskRequestDTO.description());
            task.setAppraisable(taskRequestDTO.appraisable());
            task.setStartDate(taskRequestDTO.startDate());
            task.setEndDate(taskRequestDTO.endDate());

            if(taskRequestDTO.appraisable()){
                task.setSelfRating(taskRequestDTO.selfRating());
                Appraisal appraisal = appraisalRepo.findByEmployeeIdAndAppraisalStatus(employeeId, AppraisalStatus.INITIATED);
                task.setAppraisal(appraisal);
            }
            return new TaskResponseDTOMapper().apply(taskRepo.save(task)) ;
        }

        throw  new ResourceNotFoundException("Employee with id "+employeeId+" is not found.");
    }

    public TaskResponseDTO rateTaskBySelf(Integer taskId, Double taskRating){
        Task task = taskRepo.findById(taskId).orElse(null);
        if(task!=null){
            task.setSelfRating(taskRating);
            return new TaskResponseDTOMapper().apply(taskRepo.save(task));
        }

        throw  new ResourceNotFoundException("Task with id "+taskId+" is not found.");
    }

    public TaskResponseDTO rateTaskByAdmin(Integer taskId, Double taskRating){
        Task task = taskRepo.findById(taskId).orElse(null);
        if(task!=null){
            task.setAdminRating(taskRating);
            return new TaskResponseDTOMapper().apply(taskRepo.save(task));
        }

        throw  new ResourceNotFoundException("Task with id "+taskId+" is not found.");
    }

    public String deleteTask(Integer taskId) {
        Task task = taskRepo.findById(taskId).orElse(null);
        if(task!=null){
            taskRepo.delete(task);
            return "success";
        }

        throw  new ResourceNotFoundException("Task with id "+taskId+" is not found.");
    }
}
