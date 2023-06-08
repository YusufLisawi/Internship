package com.nttdata.beca.service;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.dto.CandidateDTO;

import java.util.List;

import com.nttdata.beca.dto.InterviewDTO;
import com.nttdata.beca.dto.ScoreDTO;
import com.nttdata.beca.entity.Interview;

public interface CandidateService extends GenericService<CandidateDTO, Long> {

    List<CandidateDTO> findByEmail(String email);
    List<CandidateDTO> findByPreselectionStatus();
    List<CandidateDTO> findBySession(Long sessionId);
    List<String> findAllUniversities();
    List<String> findAllCities();
    List<CandidateDTO> getCurrentSessionCandidates();
    List<CandidateDTO> getCurrentSessionInternshipCandidates();
    List<CandidateDTO> getFinalList(int admittedNumber);
    List<CandidateDTO> getWaiting(int admittedNumber,int waitingNumber);
    List<CandidateDTO> getAdmitted();
    CandidateDTO addCandidate(CandidateDTO candidateDTO) throws Exception;
    CandidateDTO updateCandidate(CandidateDTO candidateDTO);
    CandidateDTO moveWaitingListToSession(CandidateDTO candidateDTO,Long sessionId);
    Long countPreselectedStatus();
    Long countAdmittedPreviousSession();
    Long countInSession(Long sessionId);
    Long preselectedCount();
    Long admittedCount();
    Long countEmail(String email);
    int setAdmitted(int nbAdmitted);
    MessageResponse delete(Long candidateDTO);
    MessageResponse setPreselectionStatus(Long candidateId,Boolean preselectionStatus);
    CandidateDTO saveScore(Long candidateId, ScoreDTO scoreDTO);
    CandidateDTO addInternshipCandidate(CandidateDTO candidateDTO) throws Exception;
    List<CandidateDTO> findAcceptedInternshipCandidate();
    Long countAdmittedPreviousInternshipSession();
    void divideCandidates(List<InterviewDTO> DTOS);
  int SetIdInterview(CandidateDTO candidate);
    int SetTimeInterview(int candidateId, String numberOfInterviews);

    List<CandidateDTO> findByInterview(Long idInterview);


}
