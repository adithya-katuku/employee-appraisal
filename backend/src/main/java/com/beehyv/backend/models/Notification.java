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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationId;

    private String notification;
    private String description;

    @ManyToOne()
    @JsonIgnore
    private Employee employee;

    public Notification(String notification, String description){
        this.notification = notification;
        this.description = description;
    }
}
