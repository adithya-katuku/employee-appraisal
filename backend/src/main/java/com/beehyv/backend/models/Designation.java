package com.beehyv.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
public class Designation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int designationId;
    private String designation;

    @ManyToMany
    @JsonIgnore
    private List<Attribute> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "designation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employee> employees = new ArrayList<>();

    public Designation(String designation, List<Attribute> attributes){
        this.designation = designation;
        this.attributes = attributes;
    }
}
