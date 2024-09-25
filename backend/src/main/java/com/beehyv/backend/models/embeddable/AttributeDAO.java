package com.beehyv.backend.models.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AttributeDAO {
    private String name;
    private Double rating;
}
