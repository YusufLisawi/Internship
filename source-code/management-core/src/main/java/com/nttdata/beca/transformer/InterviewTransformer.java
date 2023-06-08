package com.nttdata.beca.transformer;

import com.nttdata.beca.dto.InterviewDTO;
import com.nttdata.beca.dto.RecruiterDTO;
import com.nttdata.beca.dto.SessionDTO;
import com.nttdata.beca.entity.Interview;
import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.entity.Session;

public class InterviewTransformer extends Transformer<Interview, InterviewDTO> {

    Transformer<Session, SessionDTO> sessionTransformer = new SessionTransformer();

    @Override
    public Interview toEntity(InterviewDTO dto) {

        if (dto == null) {
            return null;
        } else {
            Interview interview = new Interview();
            interview.setInterviewId(dto.getInterviewId());
            interview.setDate(dto.getDate());
            interview.setSession(sessionTransformer.toEntity(dto.getSession()));
            return interview;
        }
    }

    @Override
    public InterviewDTO toDTO(Interview entity) {

        if (entity == null)
            return null;
        else {
          Recruiter r =  entity.getRecruiter();
            Transformer<Recruiter, RecruiterDTO> recruiterTransformer = new RecruiterTransformer();
            Transformer<Session, SessionDTO> sessionTransformer = new SessionTransformer();
            return new InterviewDTO(
                    entity.getInterviewId(),
                    entity.getDate(),
                    sessionTransformer.toDTO(entity.getSession()),
                    recruiterTransformer.toDTO(r)

            );

        }
    }

}
