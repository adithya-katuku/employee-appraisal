package com.beehyv.backend.dto.request;

import jakarta.validation.constraints.*;

import java.util.Date;

public record TaskRequestDTO(
        Integer taskId,

        @NotBlank(message = "Task title is required and cannot be empty.")
        @Size(max = 100, message = "Task title must not exceed 100 characters.")
        String taskTitle,

        @NotBlank(message = "Description is required and cannot be empty.")
        @Size(max = 500, message = "Description must not exceed 500 characters.")
        String description,

        @NotNull(message = "Start date is required.")
        @PastOrPresent(message = "Start date cannot be in the future.")
        Date startDate,

        @NotNull(message = "End date is required.")
        @PastOrPresent(message = "End date cannot be in the future.")
        Date endDate,

        boolean appraisable,

        Double selfRating
) {
}
