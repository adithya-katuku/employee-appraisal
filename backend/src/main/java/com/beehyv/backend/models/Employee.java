package com.beehyv.backend.models;

import com.beehyv.backend.enums.Role;
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
    private int employeeId;
    private String name;
    private String email;
    private Date joiningDate;
    private Role role;
    private String password;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Designation designation;

    @ElementCollection
    @CollectionTable(name = "employee_attributes", joinColumns = @JoinColumn(name = "employee_id"))
    Map<String, Integer> attributes = new HashMap<>();

    @OneToMany(mappedBy = "employee",  cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();
}
