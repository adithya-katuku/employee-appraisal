package com.beehyv.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
public class Designation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer designationId;
    private String designation;

    @ManyToMany
    private List<Attribute> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "designation", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Employee> employees = new ArrayList<>();

}
