package com.beehyv.backend.models;

import com.beehyv.backend.models.enums.AppraisalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ToString
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
public class Appraisal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer adminId;
    private Integer employeeId;
    private AppraisalStatus appraisalStatus = AppraisalStatus.PENDING;
    private Integer appraisalYear;
    private Double rating = (double) 0;
    private Date startDate;
    private Date endDate;

    @ElementCollection
    @CollectionTable(name = "appraisal_attributes", joinColumns = @JoinColumn(name = "appraisal_id"))
    private Map<String, Double> attributes = new HashMap<>();
}
