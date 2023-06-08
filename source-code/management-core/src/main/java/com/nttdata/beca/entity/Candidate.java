package com.nttdata.beca.entity;


import javax.persistence.*;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nttdata.beca.entity.abstractEntity.AbstractEntity;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidate")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Candidate extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Long candidateId;

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Column(length = 50)
    private String gender;

    @Column(length = 50)
    private String email;

    @Column(length = 50)
    private String phoneNumber;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String anapec;

    private String CVname;

    @Column(length = 100)
    private String diplome;

    private String finalAdmissionStatus;

    @Column(nullable = true)
    private Boolean preselectionStatus;

    private String university;

    private String timeOfInterview;

    private String type;

    @ManyToOne
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "score_id")
    private Score score;


    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;


    @OneToMany(mappedBy = "candidate")
    @JsonIgnore
    private List<Internship> internship = new ArrayList<>();

    private boolean deleted;

}
