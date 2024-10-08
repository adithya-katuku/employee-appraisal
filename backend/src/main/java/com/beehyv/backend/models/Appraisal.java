package com.beehyv.backend.models;

import com.beehyv.backend.models.embeddable.AttributeDAO;
import com.beehyv.backend.models.enums.AppraisalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.stereotype.Component;

import java.util.*;

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
    private AppraisalStatus appraisalStatus;
    private Double rating;
    private Date startDate;
    private Date endDate;

    @ElementCollection
    @CollectionTable(name = "appraisal_attributes", joinColumns = @JoinColumn(name = "appraisal_id"))
    private List<AttributeDAO> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "appraisal", cascade = CascadeType.PERSIST)
    private List<Task> tasks;
}
