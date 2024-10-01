package com.beehyv.backend.repositories;

import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Integer> {
    List<Notification> findByEmployeeOrderByNotificationIdDesc(Employee employee);
}
