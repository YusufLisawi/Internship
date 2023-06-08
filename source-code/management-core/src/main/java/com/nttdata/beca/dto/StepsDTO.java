package com.nttdata.beca.dto;


import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class StepsDTO {
    
    private Long stepsId;
    private String stepName;
    private String stepStatus;
    private RecruiterDTO recruiter;

}
