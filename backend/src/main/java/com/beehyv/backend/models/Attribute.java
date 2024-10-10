package com.beehyv.backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attributeId;
    private  String attribute;
}
