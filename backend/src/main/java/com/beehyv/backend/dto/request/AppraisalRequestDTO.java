package com.beehyv.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.util.Date;

public record AppraisalRequestDTO(

        @NotNull(message = "Start date is required.")
        @PastOrPresent(message = "Start date cannot be in the future.")
        Date startDate,

        @NotNull(message = "End date is required.")
        @PastOrPresent(message = "End date cannot be in the future.")
        Date endDate
) {

}
