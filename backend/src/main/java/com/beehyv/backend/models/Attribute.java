package com.beehyv.backend.models;

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
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attributeId;
    private  String attribute;
    private int rating = 0;

    public Attribute(String attribute){
        this.attribute = attribute;
    }

    public Attribute copy(){
        return new Attribute(this.attributeId, this.attribute, this.rating);
    }
}
