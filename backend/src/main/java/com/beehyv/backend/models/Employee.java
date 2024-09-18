package com.beehyv.backend.models;

import com.beehyv.backend.models.enums.AppraisalStatus;
import com.beehyv.backend.models.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.*;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
public class Employee {
    @Id
    private Integer employeeId;
    private String name;
    private String email;
    private Date joiningDate;
    private List<Role> roles;
    private String password;
    private AppraisalStatus appraisalStatus = AppraisalStatus.PENDING;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "designation_id")
    private Designation designation;

    @ElementCollection
    @CollectionTable(name = "employee_attributes", joinColumns = @JoinColumn(name = "employee_id"))
    Map<String, Integer> attributes = new HashMap<>();

}
