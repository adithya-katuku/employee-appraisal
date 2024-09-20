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
    private List<Employee> employees = new ArrayList<>();

    public Designation(String designation){
        this.designation = designation;
    }

    public Designation(String designation, List<Attribute> attributes){
        this.designation = designation;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "Designation{" +
                "designationId=" + designationId +
                ", designation='" + designation + '\'' +
                '}';
    }
}
