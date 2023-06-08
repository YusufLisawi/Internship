package com.nttdata.beca.entity;

import java.util.ArrayList;
import java.util.List;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.nttdata.beca.entity.abstractEntity.AbstractEntity;
import lombok.*;

@Entity
@Table(name = "score_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreConfig extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scoreConfig_id")
    private Long scoreConfigId;

    private int peopleDenominator;

    private int softSkillsDenominator;

    private int technicalTestDenominator;

    private int technicalInterviewDenominator;

    private int peopleWeight;

    private int softSkillsWeight;

    private int technicalTestWeight;

    private int technicalInterviewWeight;

    private String status;
}
