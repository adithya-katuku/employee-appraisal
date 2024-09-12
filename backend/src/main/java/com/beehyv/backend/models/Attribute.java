package com.beehyv.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attributeId;
    private  String attribute;

    @ManyToMany
    @JsonIgnore
    private List<Designation> designations;

    public Attribute(String attribute){
        this.attribute = attribute;
    }
}
