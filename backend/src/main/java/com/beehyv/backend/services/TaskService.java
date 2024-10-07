package com.beehyv.backend.services;

import com.beehyv.backend.dto.mappers.TaskResponseDTOMapper;
import com.beehyv.backend.dto.request.TaskRequestDTO;
import com.beehyv.backend.dto.response.TaskResponseDTO;
import com.beehyv.backend.exceptions.InvalidInputException;
import com.beehyv.backend.exceptions.ResourceNotFoundException;
import com.beehyv.backend.models.Appraisal;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Task;
import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private AppraisalRepo appraisalRepo;

    public List<TaskResponseDTO> getTasks(Employee employee){
        List<Task> tasks = taskRepo.findByEmployeeOrderByTaskIdDesc(employee);
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
            task.setAdminRating(null);
            if(taskRequestDTO.appraisable()){
                task.setSelfRating(taskRequestDTO.selfRating());
                Appraisal appraisal = appraisalRepo.findByEmployeeIdAndAppraisalStatus(employeeId, AppraisalStatus.INITIATED);
                task.setAppraisal(appraisal);
            }
            return new TaskResponseDTOMapper().apply(taskRepo.save(task)) ;
        }

        throw  new ResourceNotFoundException("Employee with id "+employeeId+" is not found.");
    }
    public TaskResponseDTO updateTask(Integer employeeId, TaskRequestDTO taskRequestDTO) {
        if(taskRequestDTO.taskId()==null){
            throw new InvalidInputException("Invalid taskId.");
        }
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee==null){
            throw  new ResourceNotFoundException("Employee with id "+employeeId+" is not found.");
        }
        Task task = taskRepo.findById(taskRequestDTO.taskId()).orElse(null);
        if(task==null){
            throw  new ResourceNotFoundException("Task with id "+taskRequestDTO.taskId()+" is not found.");
        }
        if(!Objects.equals(task.getEmployee().getEmployeeId(), employeeId)){
            throw  new InvalidInputException("Task is not done by the requested employee.");
        }
        if(task.getAppraisal()!=null && task.getAppraisal().getAppraisalStatus()!=AppraisalStatus.INITIATED){
            throw  new InvalidInputException("This task cannot be edited.");
        }

        task.setTaskTitle(taskRequestDTO.taskTitle());
        task.setDescription(taskRequestDTO.description());
        task.setAppraisable(taskRequestDTO.appraisable());
        task.setStartDate(taskRequestDTO.startDate());
        task.setEndDate(taskRequestDTO.endDate());

        if(taskRequestDTO.appraisable()){
            task.setSelfRating(taskRequestDTO.selfRating());
            if(task.getAppraisal()==null){
                Appraisal appraisal = appraisalRepo.findByEmployeeIdAndAppraisalStatus(employeeId, AppraisalStatus.INITIATED);
                task.setAppraisal(appraisal);
            }
        }
        else{
            task.setSelfRating(null);
            task.setAppraisal(null);
        }
        return new TaskResponseDTOMapper().apply(taskRepo.save(task));
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
        if(task==null){
            throw  new ResourceNotFoundException("Task with id "+taskId+" is not found.");
        }
        if(task.getAppraisal()!=null && task.getAppraisal().getAppraisalStatus()!=AppraisalStatus.INITIATED){
            throw  new InvalidInputException("This task cannot be deleted.");
        }
        taskRepo.delete(task);
        return "success";
    }

    public void addAppraisableTasksToAppraisalForm(Integer employeeId, Appraisal appraisal) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if(employee==null){
            throw  new ResourceNotFoundException("Employee with id "+employeeId+" is not found.");
        }
        List<Task> tasks = taskRepo.findByEmployee(employee);
        for(Task task: tasks){
            if( task.getAppraisal()==null && task.isAppraisable()){
                task.setAppraisal(appraisal);
                taskRepo.save(task);
            }
        }
    }


}
