package com.beehyv.backend.models;

import com.beehyv.backend.models.enums.AppraisalEligibility;
import com.beehyv.backend.models.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.*;

@ToString
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
@Table
public class Employee {
    @Id
    private Integer employeeId;
    private String name;
    private String email;
    private Date joiningDate;
    private List<Role> roles;
    private String password;
    private Date previousAppraisalDate = this.joiningDate;
    private AppraisalEligibility appraisalEligibility = AppraisalEligibility.NOT_ELIGIBLE;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "designation_id")
    private Designation designation;
}
