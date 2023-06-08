package com.nttdata.beca.dto;


import com.nttdata.beca.entity.enums.EInternshipType;
import lombok.*;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InternshipDTO {


    private long internshipId;

    private String subject;

    private LocalDate startDate;

    private LocalDate endDate;

    private String internshipStatus;

    private float reportRating;

    private float internshipRating;

    private boolean deleted;

    private SessionDTO session;

    private CandidateDTO candidate;

    private EInternshipType type;

    private String supervisor;

    private String supervisorPhone;

    private String supervisorEmail;

    private boolean lastReportReminderSent;

    private LocalDate lastReportReminderDate;

}
