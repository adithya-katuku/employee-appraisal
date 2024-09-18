package com.beehyv.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;
    private String taskTitle;
    private String description;
    private float duration;
    private boolean appraisable;
    private int selfRating = 0;
    private int adminRating = 0;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Task(String taskTitle, String description, float duration, boolean appraisable){
        this.taskTitle = taskTitle;
        this.description = description;
        this.duration = duration;
        this.appraisable = appraisable;
    }
}
