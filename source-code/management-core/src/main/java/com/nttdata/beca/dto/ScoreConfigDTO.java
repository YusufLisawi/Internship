package com.nttdata.beca.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class ScoreConfigDTO {

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
