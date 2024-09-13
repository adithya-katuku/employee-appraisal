package com.beehyv.backend.models;

import com.beehyv.backend.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "employee_attribute",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private List<Attribute> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "employee",  cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();
}
