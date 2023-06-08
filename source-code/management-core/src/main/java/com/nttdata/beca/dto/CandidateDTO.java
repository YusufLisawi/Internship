package com.nttdata.beca.dto;

import com.nttdata.beca.entity.Interview;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class CandidateDTO {

    private Long candidateId;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phoneNumber;
    private String city;
    private String anapec;
    private String CVname;
    private String diplome;
    private String finalAdmissionStatus;
    private Boolean preselectionStatus;
    private String university;
    private String timeOfInterview;
    private InterviewDTO interview;
    private SessionDTO session;
    private ScoreDTO score;
    private String type;
    private boolean deleted;

}
