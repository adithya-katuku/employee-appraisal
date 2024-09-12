package com.beehyv.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

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
    private String role;
    private String password;

    @ManyToOne()
    @JoinColumn(name = "designation_id")
    private Designation designation;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Notification> notifications;
}
