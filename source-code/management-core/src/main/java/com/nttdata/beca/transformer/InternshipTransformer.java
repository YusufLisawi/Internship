package com.nttdata.beca.transformer;

import com.nttdata.beca.dto.*;
import com.nttdata.beca.entity.*;

public class InternshipTransformer extends Transformer<Internship, InternshipDTO> {

    Transformer<Session, SessionDTO> sessionTransformer = new SessionTransformer();
    Transformer<Candidate, CandidateDTO> candidateTransformer = new CandidateTransformer();

    @Override
    public Internship toEntity(InternshipDTO dto) {
        if (dto == null)
            return null;
        else {

            Internship internship = new Internship();
            internship.setInternshipId(dto.getInternshipId());
            internship.setSubject(dto.getSubject());
            internship.setStartDate(dto.getStartDate());
            internship.setEndDate(dto.getEndDate());
            internship.setInternshipStatus(dto.getInternshipStatus());
            internship.setInternshipRating(dto.getInternshipRating());
            internship.setReportRating(dto.getReportRating());
            internship.setType(dto.getType());
            internship.setSupervisor(dto.getSupervisor());
            internship.setSupervisorPhone(dto.getSupervisorPhone());
            internship.setSupervisorEmail(dto.getSupervisorEmail());
            internship.setDeleted(dto.isDeleted());
            internship.setLastReportReminderDate(dto.getLastReportReminderDate());
            internship.setLastReportReminderSent(dto.isLastReportReminderSent());
            internship.setSession(sessionTransformer.toEntity(dto.getSession()));
            internship.setCandidate(candidateTransformer.toEntity(dto.getCandidate()));
            return internship;
        }
    }

    @Override
    public InternshipDTO toDTO(Internship entity) {
        if (entity == null)
            return null;
        else {

            InternshipDTO internshipDTO = new InternshipDTO();
            internshipDTO.setInternshipId(entity.getInternshipId());
            internshipDTO.setSubject(entity.getSubject());
            internshipDTO.setStartDate(entity.getStartDate());
            internshipDTO.setEndDate(entity.getEndDate());
            internshipDTO.setInternshipStatus(entity.getInternshipStatus());
            internshipDTO.setInternshipRating(entity.getInternshipRating());
            internshipDTO.setReportRating(entity.getReportRating());
            internshipDTO.setType(entity.getType());
            internshipDTO.setSupervisor(entity.getSupervisor());
            internshipDTO.setSupervisorPhone(entity.getSupervisorPhone());
            internshipDTO.setSupervisorEmail(entity.getSupervisorEmail());
            internshipDTO.setDeleted(entity.isDeleted());
            internshipDTO.setLastReportReminderDate(entity.getLastReportReminderDate());
            internshipDTO.setLastReportReminderSent(entity.isLastReportReminderSent());
            internshipDTO.setSession(sessionTransformer.toDTO(entity.getSession()));
            internshipDTO.setCandidate(candidateTransformer.toDTO(entity.getCandidate()));
            return internshipDTO;
        }
    }
}
