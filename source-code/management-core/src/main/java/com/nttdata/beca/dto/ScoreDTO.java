package com.nttdata.beca.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class ScoreDTO {

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
    private Boolean isAccepted;


}
