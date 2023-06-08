package com.nttdata.beca.service;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.dto.RoleDTO;
import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.dto.RecruiterDTO;

import java.util.List;

public interface RecruiterService extends GenericService<RecruiterDTO, Long> {

    Iterable<RecruiterDTO> findArchived();
    RecruiterDTO findByUsername(String username);
    RecruiterDTO findByEmail(String email);
    boolean existsByEmail(String email);
    RecruiterDTO save(RecruiterDTO recruiterDTO, boolean encodePassword);
    RecruiterDTO updateRecruiter(RecruiterDTO recruiterDTO);
    List<RecruiterDTO> findUsers();
    MessageResponse restore(RecruiterDTO recruiterDTO);
    MessageResponse delete(Long recruiterId);
    List<RoleDTO> getAllRoles();
}
