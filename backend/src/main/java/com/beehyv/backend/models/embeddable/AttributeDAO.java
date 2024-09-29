package com.beehyv.backend.models.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AttributeDAO {
    @NotEmpty
    private String name;
    @NotNull
    private Double rating;
}
