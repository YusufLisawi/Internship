package com.nttdata.beca.entity;



import com.nttdata.beca.entity.enums.EInternshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Internship {

    @Id
    @GeneratedValue
    @Column(name = "internship_id")
    private long internshipId;

    @Column(length = 60)
    private String subject;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(length = 30)
    private String internshipStatus;

    private float reportRating;

    private float internshipRating;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EInternshipType type;

    private String supervisor;

    private String supervisorPhone;

    private String supervisorEmail;

    @Column
    private boolean deleted;

    @Column(name = "last_report_reminder_sent")
    private boolean lastReportReminderSent;

    @Column(name = "last_report_reminder_date")
    private LocalDate lastReportReminderDate;

    @ManyToOne
    @JoinColumn(name = "session_id")
    //@JsonIgnore
    private Session session;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;


}
