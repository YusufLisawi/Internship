package com.nttdata.beca.entity;



           
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.nttdata.beca.entity.abstractEntity.AbstractEntity;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "interview")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Interview extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "interview_id")
	private Long interviewId;

	private Date date;

	@ManyToOne
	@JoinColumn(name = "session_id")
	@JsonIgnore
	private Session session;
	@ManyToOne
	@JoinColumn(name = "recruiter_id")
	@JsonIgnore
	private Recruiter recruiter;

}
