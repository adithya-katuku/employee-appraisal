package com.beehyv.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;
    private String taskTitle;

    @Column(length = 1000)
    private String description;
    private Date startDate;
    private Date endDate;
    private boolean appraisable;
    private Double selfRating;
    private Double adminRating;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "appraisal_id")
    @JsonIgnore
    @ToString.Exclude
    private Appraisal appraisal;

}
