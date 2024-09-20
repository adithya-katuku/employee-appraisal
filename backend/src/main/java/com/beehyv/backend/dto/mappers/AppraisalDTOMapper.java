package com.beehyv.backend.dto.mappers;

import com.beehyv.backend.dto.response.AppraisalDTO;
import com.beehyv.backend.models.Appraisal;

import java.util.function.Function;

public class AppraisalDTOMapper implements Function<Appraisal, AppraisalDTO> {
    @Override
    public AppraisalDTO apply(Appraisal appraisal) {
        return new AppraisalDTO(
                appraisal.getAppraisalYear()
        );
    }
}
