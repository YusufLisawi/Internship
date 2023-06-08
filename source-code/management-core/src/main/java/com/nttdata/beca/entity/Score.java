package com.nttdata.beca.entity;

           
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.nttdata.beca.entity.abstractEntity.AbstractEntity;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "score")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Score extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long scoreId;

    private String peopleDesc;

    private String softSkillsDesc;

    private String technicalDesc;

    private double finalScore;

    private String englishLevel;

    private String spanishLevel;

    private double peopleScore;

    private double softSkillsScore;

    private double technicalTestScore;

    private double technicalInterviewScore;

    @Column(nullable = true)
    private Boolean isAccepted;

    @OneToMany(mappedBy = "score", orphanRemoval = true)
    @JsonIgnore
    private List<Candidate> candidate = new ArrayList<>();

}
