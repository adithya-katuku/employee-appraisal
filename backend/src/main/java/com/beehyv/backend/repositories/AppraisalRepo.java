package com.beehyv.backend.repositories;

import com.beehyv.backend.models.Appraisal;
import com.beehyv.backend.models.enums.AppraisalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppraisalRepo extends JpaRepository<Appraisal, Integer> {
    Appraisal findByEmployeeIdAndAppraisalStatus(Integer employeeId, AppraisalStatus appraisalStatus);

    List<Appraisal> findByEmployeeIdOrderByEndDateDesc(Integer employeeId);

    List<Appraisal> findByAppraisalStatus(AppraisalStatus appraisalStatus);
}
