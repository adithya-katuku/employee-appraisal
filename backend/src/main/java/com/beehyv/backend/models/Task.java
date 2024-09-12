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
    private String task;
    private String description;
    private float duration;
    private boolean appraisable;

    @ManyToOne()
    @JsonIgnore
    private Employee employee;

    public Task(String task, String description, float duration, boolean appraisable){
        this.task = task;
        this.description = description;
        this.duration = duration;
        this.appraisable = appraisable;
    }
}
