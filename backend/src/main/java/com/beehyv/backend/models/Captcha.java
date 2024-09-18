package com.beehyv.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Date;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
public class Captcha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer captchaId;
    private String text;
    private Date expiry;

    @Transient
    private String image;
}
