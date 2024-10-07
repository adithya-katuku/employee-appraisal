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
public class RefreshToken {
    @Id
    private Integer employeeId;
    private String token;
}
