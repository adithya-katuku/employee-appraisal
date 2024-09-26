package com.beehyv.backend.dto.response;

import com.beehyv.backend.models.embeddable.AttributeDAO;
import com.beehyv.backend.models.enums.AppraisalStatus;

import java.util.Date;
import java.util.List;

public record AppraisalDTO(
        Integer id,
        AppraisalStatus appraisalStatus,
        Double rating,
        Date startDate,
        Date endDate,
        List<AttributeDAO> attributes
) {
}
