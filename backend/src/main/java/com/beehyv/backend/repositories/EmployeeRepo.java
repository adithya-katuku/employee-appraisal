package com.beehyv.backend.repositories;

import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
    Employee findByEmail(String email);

    @Query(value = "select * from employee e where :role = any(e.roles)", nativeQuery = true)
    List<Employee> findByRole(Role role);

    @Query(value = "select * from employee e where :role >= all(e.roles) and appraisal_status = :appraisalStatus and joining_date < now() - interval '1 year' ", nativeQuery = true)
    List<Employee> findByEmployeesWhoAreEligibleForAppraisal(Role role, AppraisalStatus appraisalStatus);
}
