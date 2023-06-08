package com.nttdata.beca.transformer;

import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.entity.Session;
import com.nttdata.beca.repository.RecruiterRepository;
import com.nttdata.beca.dto.RecruiterDTO;
import com.nttdata.beca.dto.SessionDTO;

import org.springframework.beans.factory.annotation.Autowired;

public class SessionTransformer extends Transformer<Session, SessionDTO> {

    @Autowired
    RecruiterRepository recruiterRepository;

    @Override
    public Session toEntity(SessionDTO dto) {
        Transformer<Recruiter, RecruiterDTO> recruiterTransformer = new RecruiterTransformer();
        if (dto == null)
            return null;
        else {
            Session session = new Session();
            session.setSessionId(dto.getSessionId());
            session.setSessionName(dto.getSessionName());
            session.setSessionDate(dto.getSessionDate());
            session.setDayInterviewNumber(dto.getDayInterviewNumber());
            session.setAdmittedNumber(dto.getAdmittedNumber());
            session.setEliminatingMark(dto.getEliminatingMark());
            session.setEnglishLevelRequire(dto.getEnglishLevelRequire());
            session.setStartTimeOfInterview(dto.getStartTimeOfInterview());
            session.setEndTimeOfInterview(dto.getEndTimeOfInterview());
            session.setTestDuration(dto.getTestDuration());
            session.setTechnology(dto.getTechnology());
            session.setSessionStatus(dto.getSessionStatus());
            session.setType(dto.getType());
            session.setRecruiter(recruiterTransformer.toEntity(dto.getRecruiter()));
            return session;
        }
    }

    @Override
    public SessionDTO toDTO(Session entity) {
        if (entity == null)
            return null;
        else {
            Transformer<Recruiter, RecruiterDTO> recruiterTransformer = new RecruiterTransformer();

            return new SessionDTO(entity.getSessionId(),
                    entity.getSessionName(),
                    entity.getSessionDate(),
                    entity.getDayInterviewNumber(),
                    entity.getAdmittedNumber(),
                    entity.getEliminatingMark(),
                    entity.getEnglishLevelRequire(),
                    entity.getStartTimeOfInterview(),
                    entity.getEndTimeOfInterview(),
                    entity.getTestDuration(),
                    entity.getTechnology(),
                    entity.getSessionStatus(),
                    entity.getType(),
                    recruiterTransformer.toDTO(entity.getRecruiter()));

        }
    }

}
