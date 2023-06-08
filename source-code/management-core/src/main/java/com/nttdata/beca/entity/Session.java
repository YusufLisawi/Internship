package com.nttdata.beca.entity;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.nttdata.beca.entity.abstractEntity.AbstractEntity;
import lombok.*;

@Entity
@Table(name = "session")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "session_id")
	private Long sessionId;

	@Column(nullable = false, length = 50)
	private String sessionName;

	private Date sessionDate;

	@Column(length = 50)
	private String technology;

	private int dayInterviewNumber;
	private int admittedNumber;
	private int eliminatingMark;
	private String englishLevelRequire;
	private String startTimeOfInterview;
	private String endTimeOfInterview;
	private int testDuration;

	@Column(nullable = false, length = 50)
	private String sessionStatus;

	@Column(nullable = true, length = 50)
	private String type;

	@ManyToOne
	@JoinColumn(name = "recruiter_id")
	private Recruiter recruiter;

	@OneToMany(mappedBy = "session", orphanRemoval = true)
	@JsonIgnore
	private List<Candidate> candidate = new ArrayList<>();

	@OneToMany(mappedBy = "session", orphanRemoval = true)
	@JsonIgnore
	private List<Interview> interview = new ArrayList<>();

	@OneToMany(mappedBy = "session", orphanRemoval = true)
	@JsonIgnore
	private List<Internship> internship = new ArrayList<>();

}
