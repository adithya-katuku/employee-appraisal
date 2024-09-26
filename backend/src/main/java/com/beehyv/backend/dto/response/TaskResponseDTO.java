package com.beehyv.backend.dto.response;

import java.util.Date;

public record TaskResponseDTO(
        Integer taskId,
        String taskTitle,
        String description,
        Date startDate,
        Date endDate,
        boolean appraisable,
        Double selfRating,
        Double adminRating,
        Integer appraisalId,
        boolean editable
) {
}
