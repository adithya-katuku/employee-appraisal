package com.beehyv.backend.dto.request;

import com.beehyv.backend.models.embeddable.AttributeDAO;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RateAttributeRequestDTO(
        @NotEmpty
        List<AttributeDAO> attributes
) {
}
