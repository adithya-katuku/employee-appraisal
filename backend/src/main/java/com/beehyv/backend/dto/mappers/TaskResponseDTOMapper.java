package com.beehyv.backend.dto.mappers;

import com.beehyv.backend.dto.response.TaskResponseDTO;
import com.beehyv.backend.models.Task;

import java.util.function.Function;

public class TaskResponseDTOMapper implements Function<Task, TaskResponseDTO> {
    @Override
    public TaskResponseDTO apply(Task task) {
        return new TaskResponseDTO(
                task.getTaskId(),
                task.getTaskTitle(),
                task.getDescription(),
                task.getStartDate(),
                task.getEndDate(),
                task.isAppraisable(),
                task.isAppraisable()? task.getSelfRating() : null,
                task.isAppraisable()? task.getAdminRating() : null,
                task.isAppraisable()? new AppraisalDTOMapper().apply(task.getAppraisal()):null
        );
    }
}
