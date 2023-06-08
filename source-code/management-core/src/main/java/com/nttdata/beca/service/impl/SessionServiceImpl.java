package com.nttdata.beca.service.impl;

import com.nttdata.beca.dto.SessionDTO;
import com.nttdata.beca.entity.Session;
import com.nttdata.beca.exception.BecaTrainingNotFoundException;
import com.nttdata.beca.repository.SessionRepository;
import com.nttdata.beca.service.SessionService;
import com.nttdata.beca.transformer.SessionTransformer;
import com.nttdata.beca.transformer.Transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionServiceImpl extends GenericServiceImpl<Session, SessionDTO, Long> implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;
    private static Transformer<Session, SessionDTO> sessionTransformer = new SessionTransformer();

    public SessionServiceImpl() {
        super(sessionTransformer);
    }

    @Override
    public List<SessionDTO> findAll() {
        return sessionTransformer.toDTOList(sessionRepository.findAll());
    }

    @Override
    public SessionDTO findSessionById(Long id) {
        return sessionTransformer.toDTO(sessionRepository.findBySessionId(id));
    }

    @Override
    public SessionDTO findSessionByName(String sessionName) {
        return sessionTransformer.toDTO(sessionRepository.findBySessionName(sessionName));
    }
    public SessionDTO findSessionByNameAndType(String sessionName,String type) {
        return sessionTransformer.toDTO(sessionRepository.findBySessionNameAndType(sessionName,type));
    }
    @Override
    public SessionDTO findSessionByStatus(String sessionStatus) {
        return sessionTransformer.toDTO(sessionRepository.findBySessionStatus(sessionStatus));
    }

    @Override
    public Long getLatestSessionId() {
        return sessionRepository.getLatestSessionId();
    }

    @Override
    public List<String> findAllTechnologies() {
        List<String> technologies = sessionRepository.findAllTechnologies();
        if (technologies == null || technologies.isEmpty()) {
            throw new BecaTrainingNotFoundException("There is no technology registered in the database");
        }
        return technologies;
    }

    @Override
    public SessionDTO addSession(SessionDTO sessionDTO) throws Exception {
        SessionDTO dto=findSessionByNameAndType(sessionDTO.getSessionName(),"recruitment");
        if (dto != null) {
            throw new Exception("Session with this name already exists");
        }
        if (sessionDTO.getDayInterviewNumber() == 0) {
            sessionDTO.setDayInterviewNumber(40);
        }
        if (sessionDTO.getAdmittedNumber() == 0) {
            sessionDTO.setAdmittedNumber(20);
        }
        if (sessionDTO.getEliminatingMark() == 0) {
            sessionDTO.setEliminatingMark(5);
        }
        if (sessionDTO.getEnglishLevelRequire() == null) {
            sessionDTO.setEnglishLevelRequire("B1");
        }
        sessionDTO.setType("recruitment");
        return super.save(sessionDTO);
    }

    @Override
    public SessionDTO updateSession(SessionDTO sessionDTO) {
        SessionDTO sessionToUpdate = findSessionById(sessionDTO.getSessionId());
        sessionToUpdate.setSessionName(sessionDTO.getSessionName());
        sessionToUpdate.setTechnology(sessionDTO.getTechnology());
        sessionToUpdate.setSessionDate(sessionDTO.getSessionDate());
        return super.save(sessionToUpdate);
    }

    @Override
    public SessionDTO updateSessionRequirement(SessionDTO sessionDTO) {
        Session dto = sessionTransformer.toEntity(findSessionById(sessionDTO.getSessionId()));
        dto.setAdmittedNumber(sessionRepository.getAdmittedNumber());
        dto.setEliminatingMark(sessionDTO.getEliminatingMark());
        dto.setEnglishLevelRequire(sessionDTO.getEnglishLevelRequire());
        return sessionTransformer.toDTO(sessionRepository.save(dto));
    }

    @Override
    public SessionDTO updateSessionDailyInterview(SessionDTO sessionDTO) {
        SessionDTO dto = findSessionById(sessionDTO.getSessionId());
        dto.setDayInterviewNumber(sessionDTO.getDayInterviewNumber());
        dto.setStartTimeOfInterview(sessionDTO.getStartTimeOfInterview());
        dto.setEndTimeOfInterview(sessionDTO.getEndTimeOfInterview());
        dto.setTestDuration(sessionDTO.getTestDuration());
        return super.save(dto);
    }

    @Override
    public Boolean ToPrevious() {
        sessionRepository.toPrevious();
        return true;
    }

    @Override
    public Boolean ToClosed() {
        sessionRepository.toClosed();
        return true;
    }

    @Override
    public int getAdmittedNumber() {
        Integer result = sessionRepository.getAdmittedNumber();
        return sessionRepository.getAdmittedNumber();
    }

    @Override
    public SessionDTO findInternshipSessionByStatus(String sessionStatus) {
        return sessionTransformer.toDTO(sessionRepository.findInternshipSessionStatus(sessionStatus));
    }

    @Override
    public List<SessionDTO> findInternshipSessionAll() {
        return sessionTransformer.toDTOList(sessionRepository.findInternshipSessionAll());
    }


    @Override
    public SessionDTO addInternshipSession(SessionDTO sessionDTO) throws Exception {
        SessionDTO dto=findSessionByNameAndType(sessionDTO.getSessionName(),"internship");
        if (dto != null) {
            throw new Exception("Session with this name already exists");
        }
        sessionDTO.setType("internship");
        sessionDTO.setDayInterviewNumber(0);
        sessionDTO.setAdmittedNumber(0);
        sessionDTO.setEliminatingMark(0);
        sessionDTO.setEnglishLevelRequire("");
        return super.save(sessionDTO);
    }

    @Override
    public Boolean ToPreviousInternship() {
        sessionRepository.toPreviousInternship();
        return true;    }

    @Override
    public Boolean ToClosedInternship() {
        sessionRepository.toClosedInternship();
        return true;
    }

    @Override
    public Long getLatestInternshipSessionId() {
        return sessionRepository.getLatestInternshipSessionId();
    }

}
