package com.nttdata.beca.dto;

import java.sql.Date;
import java.sql.Time;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class SessionDTO {

	private Long sessionId;
	private String sessionName;
	private Date sessionDate;
	private int dayInterviewNumber;
	private int admittedNumber;
	private int eliminatingMark;
	private String englishLevelRequire;
	private String startTimeOfInterview;
	private String endTimeOfInterview;
	private int testDuration;
	private String technology;
	private String sessionStatus;
	private String type;
	private RecruiterDTO recruiter;


}
