package com.nttdata.beca.service;

import com.nttdata.beca.dto.SessionDTO;

import java.util.List;

public interface SessionService extends GenericService<SessionDTO, Long> {
    List<SessionDTO> findAll();
    SessionDTO findSessionById(Long id);
    SessionDTO findSessionByName(String sessionName);
    SessionDTO findSessionByStatus(String sessionStatus);
    Long getLatestSessionId();
    List<String> findAllTechnologies();
    SessionDTO addSession(SessionDTO sessionDTO) throws Exception;
    SessionDTO updateSession(SessionDTO sessionDTO);
    SessionDTO updateSessionRequirement(SessionDTO sessionDTO);
    SessionDTO updateSessionDailyInterview(SessionDTO sessionDTO);
    Boolean ToPrevious();
    Boolean ToClosed();
    int getAdmittedNumber();

    SessionDTO findInternshipSessionByStatus(String sessionStatus);

    List<SessionDTO> findInternshipSessionAll();

    SessionDTO addInternshipSession(SessionDTO sessionDTO) throws Exception;

    Boolean ToPreviousInternship();

    Boolean ToClosedInternship();

    Long getLatestInternshipSessionId();
}
