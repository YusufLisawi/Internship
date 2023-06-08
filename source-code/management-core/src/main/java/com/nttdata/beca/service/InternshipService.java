package com.nttdata.beca.service;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.dto.InternshipDTO;

import java.util.List;

public interface InternshipService {
    public List<InternshipDTO> getCurrentSessionInternships();

    InternshipDTO getInternshipsById(long internship_id);

    InternshipDTO addInternship(InternshipDTO internshipDTO) throws Exception;

    MessageResponse deleteInternshipById(long internship_id);

    InternshipDTO updateInternship(InternshipDTO internshipDTO);

    List<InternshipDTO> getAllInternships();

    List<InternshipDTO> getInternshipsBySessionId(Long sessionId);
}


