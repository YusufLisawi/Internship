package com.nttdata.beca.transformer;

import com.nttdata.beca.dto.RecruiterDTO;
import com.nttdata.beca.dto.RoleDTO;
import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.entity.Role;

public class RecruiterTransformer extends Transformer<Recruiter, RecruiterDTO> {

    private static final Transformer<Role, RoleDTO> roleTransformer = new RoleTransformer();

    @Override
    public Recruiter toEntity(RecruiterDTO dto) {
        if (dto == null) {
            return null;
        } else {
            Recruiter recruiter = new Recruiter();
            recruiter.setRecruiterId(dto.getRecruiterId());
            recruiter.setEmail(dto.getEmail());
            recruiter.setUsername(dto.getUsername());
            recruiter.setFirstName(dto.getFirstName());
            recruiter.setLastName(dto.getLastName());
            recruiter.setPassword(dto.getPassword());
            recruiter.setPhoneNumber(dto.getPhoneNumber());
            recruiter.setRole(roleTransformer.toEntityList(dto.getRole()));
            recruiter.setPicture(dto.getPicture());
           // interviewTransformer.toEntityList(dto.getInterview());
         //  sessionTransformer.toEntityList(dto.getSession());
           // stepsTransformer.toEntityList(dto.getSteps());
            return recruiter;
        }
    }

    @Override
    public RecruiterDTO toDTO(Recruiter entity) {
        if (entity == null) {
            return null;
        } else {
            return new RecruiterDTO(entity.getRecruiterId(), entity.getEmail(), entity.getUsername(),
                    entity.getFirstName(), entity.getLastName(), entity.getPassword(), entity.getPhoneNumber(), roleTransformer.toDTOList(entity.getRole()),entity.getPicture(),entity.isDeleted());
        }

    }

}
