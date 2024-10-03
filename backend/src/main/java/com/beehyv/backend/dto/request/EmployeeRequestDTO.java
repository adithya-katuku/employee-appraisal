package com.beehyv.backend.dto.request;

import com.beehyv.backend.models.enums.Role;
import jakarta.validation.constraints.*;

import java.util.Date;
import java.util.List;

public record EmployeeRequestDTO(
        @NotNull(message = "Employee ID is required.")
        Integer employeeId,

        @NotBlank(message = "Name is required.")
        @Size(min = 3, message = "Name must be at least 3 characters long.")
        String name,

        @NotBlank
        String designation,

        @NotBlank(message = "Email is required.")
        @Email(message = "Please provide a valid email address.")
        String email,

        @NotNull(message = "Joining date is required.")
        @PastOrPresent(message = "Joining date cannot be in the future.")
        Date joiningDate,

        @NotEmpty(message = "At least one role must be assigned.")
        List<Role> roles,

        @NotBlank(message = "Password is required.")
        @Size(min = 4, max = 15, message = "Password must be between 4 and 15 characters long.")
        String password
) {
}
