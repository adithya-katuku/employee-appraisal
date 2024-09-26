package com.beehyv.backend.repositories;

import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {
    List<Task> findByEmployeeOrderByTaskIdDesc(Employee employee);

    List<Task> findByEmployee(Employee employee);
}
