package com.nttdata.beca.dto;


import com.nttdata.beca.entity.Interview;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class InterviewDTO  {

    private Long interviewId;
    private Date date;
    private SessionDTO session;
    private RecruiterDTO recruiter;


}
