package com.beehyv.backend.dto.mappers;


import com.beehyv.backend.dto.response.AppraisalDTO;
import com.beehyv.backend.models.Appraisal;

import java.util.ArrayList;
import java.util.function.Function;

public class AppraisalDTOMapper implements Function<Appraisal, AppraisalDTO> {
    @Override
    public AppraisalDTO apply(Appraisal appraisal) {
        return new AppraisalDTO(
                appraisal.getId(),
                appraisal.getAppraisalStatus(),
                appraisal.getRating(),
                appraisal.getStartDate(),
                appraisal.getEndDate(),
                appraisal.getAppraisalStatus().ordinal()>=3?appraisal.getAttributes():new ArrayList<>()
        );
    }
}
