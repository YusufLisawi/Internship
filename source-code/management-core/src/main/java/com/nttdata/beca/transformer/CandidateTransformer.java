package com.nttdata.beca.transformer;

import com.nttdata.beca.dto.CandidateDTO;
import com.nttdata.beca.dto.InterviewDTO;
import com.nttdata.beca.dto.ScoreDTO;
import com.nttdata.beca.dto.SessionDTO;
import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Interview;
import com.nttdata.beca.entity.Score;
import com.nttdata.beca.entity.Session;

public class CandidateTransformer extends Transformer<Candidate, CandidateDTO> {

    @Override
    public Candidate toEntity(CandidateDTO dto) {
        if (dto == null)
            return null;
        else {
            Transformer<Session, SessionDTO> sessionTransformer = new SessionTransformer();
            Transformer<Score, ScoreDTO> scoreTransformer = new ScoreTransformer();
            Transformer<Interview, InterviewDTO> interviewTransformer = new InterviewTransformer();

            Candidate candidate = new Candidate();
            candidate.setCandidateId(dto.getCandidateId());
            candidate.setFirstName(dto.getFirstName());
            candidate.setLastName(dto.getLastName());
            candidate.setAnapec(dto.getAnapec());
            candidate.setDiplome(dto.getDiplome());
            candidate.setUniversity(dto.getUniversity());
            candidate.setCity(dto.getCity());
            candidate.setEmail(dto.getEmail());
            candidate.setCVname(dto.getCVname());
            candidate.setGender(dto.getGender());
            candidate.setType(dto.getType());
            candidate.setDeleted(dto.isDeleted());
            candidate.setPhoneNumber(dto.getPhoneNumber());
            candidate.setTimeOfInterview(dto.getTimeOfInterview());
            candidate.setFinalAdmissionStatus(dto.getFinalAdmissionStatus());
            candidate.setPreselectionStatus(dto.getPreselectionStatus());
            candidate.setSession(sessionTransformer.toEntity(dto.getSession()));
            candidate.setScore(scoreTransformer.toEntity(dto.getScore()));
            candidate.setInterview(interviewTransformer.toEntity(dto.getInterview()));
            return candidate;
        }

    }

    @Override
    public CandidateDTO toDTO(Candidate entity) {
        if (entity == null) {
            return null;
        } else {
            Transformer<Session, SessionDTO> sessionTransformer = new SessionTransformer();
            Transformer<Score, ScoreDTO> scoreTransformer = new ScoreTransformer();
            Transformer<Interview, InterviewDTO> interviewTransformer = new InterviewTransformer();

            CandidateDTO candidateDTO = new CandidateDTO();
            candidateDTO.setCandidateId(entity.getCandidateId());
            candidateDTO.setFirstName(entity.getFirstName());
            candidateDTO.setLastName(entity.getLastName());
            candidateDTO.setAnapec(entity.getAnapec());
            candidateDTO.setDiplome(entity.getDiplome());
            candidateDTO.setCity(entity.getCity());
            candidateDTO.setUniversity(entity.getUniversity());
            candidateDTO.setEmail(entity.getEmail());
            candidateDTO.setCVname(entity.getCVname());
            candidateDTO.setGender(entity.getGender());
            candidateDTO.setPhoneNumber(entity.getPhoneNumber());
            candidateDTO.setTimeOfInterview(entity.getTimeOfInterview());
            candidateDTO.setFinalAdmissionStatus(entity.getFinalAdmissionStatus());
            candidateDTO.setPreselectionStatus(entity.getPreselectionStatus());
            candidateDTO.setSession(sessionTransformer.toDTO(entity.getSession()));

            candidateDTO.setScore(scoreTransformer.toDTO(entity.getScore()));
            candidateDTO.setType(entity.getType());
            candidateDTO.setDeleted(entity.isDeleted());
            candidateDTO.setInterview(interviewTransformer.toDTO(entity.getInterview()));
            return candidateDTO;
        }
    }

}
