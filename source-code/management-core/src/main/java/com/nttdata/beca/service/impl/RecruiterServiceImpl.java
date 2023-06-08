package com.nttdata.beca.service.impl;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.dto.RecruiterDTO;
import com.nttdata.beca.dto.RoleDTO;
import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.entity.Role;
import com.nttdata.beca.entity.enums.ERole;
import com.nttdata.beca.repository.RecruiterRepository;
import com.nttdata.beca.repository.RoleRepository;
import com.nttdata.beca.service.RecruiterService;
import com.nttdata.beca.transformer.RecruiterTransformer;
import com.nttdata.beca.transformer.RoleTransformer;
import com.nttdata.beca.transformer.Transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecruiterServiceImpl extends GenericServiceImpl<Recruiter, RecruiterDTO, Long> implements RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;

    public static Transformer<Recruiter, RecruiterDTO> recruiterTransformer = new RecruiterTransformer();

    public static Transformer<Role, RoleDTO> roleTransformer = new RoleTransformer();
    public RecruiterServiceImpl() {
        super(recruiterTransformer);
    }

    @Override
    public Iterable<RecruiterDTO> findAll() {
        return recruiterTransformer.toDTOList(recruiterRepository.findByDeletedFalse());
    }

    @Override
    public Iterable<RecruiterDTO> findArchived() {
        return recruiterTransformer.toDTOList(recruiterRepository.findByDeletedTrue());
    }

    @Override
    public RecruiterDTO findByUsername(String username) {
        return recruiterTransformer.toDTO(recruiterRepository.findByUsername(username));
    }

    @Override
    public RecruiterDTO findByEmail(String email){
        return recruiterTransformer.toDTO(recruiterRepository.findByEmail(email));
    }
    @Override
    public boolean existsByEmail(String email) {
        return recruiterRepository.existsByEmail(email);
    }

    @Override
    public RecruiterDTO save(RecruiterDTO recruiterDTO, boolean encodePassword) {
        RecruiterDTO user = new RecruiterDTO(recruiterDTO.getEmail(), recruiterDTO.getUsername(), recruiterDTO.getFirstName(),
                recruiterDTO.getLastName(), recruiterDTO.getPassword(), recruiterDTO.getPhoneNumber(), recruiterDTO.getPicture(), recruiterDTO.getRole());
        if(encodePassword){
            user.setPassword(encoder.encode(recruiterDTO.getPassword()));
        }
        return super.save(user);
    }

    @Override
    public RecruiterDTO updateRecruiter(RecruiterDTO recruiterDTO) {
        RecruiterDTO dto = findById(recruiterDTO.getRecruiterId());
        dto.setEmail(recruiterDTO.getEmail());
        dto.setFirstName(recruiterDTO.getFirstName());
        dto.setLastName(recruiterDTO.getLastName());
        dto.setEmail(recruiterDTO.getEmail());
        dto.setPhoneNumber(recruiterDTO.getPhoneNumber());
        dto.setPicture(recruiterDTO.getPicture());
        dto.setRole(recruiterDTO.getRole());
        if(recruiterDTO.getPassword() != null && !recruiterDTO.getPassword().isEmpty() && !recruiterDTO.getPassword().isBlank()){
            dto.setPassword(recruiterDTO.getPassword());
            return save(dto, true);
        }
        return save(dto, false);
    }

    @Override
    public RecruiterDTO findById(Long recruiterId) {
        return recruiterTransformer.toDTO(recruiterRepository.findById(recruiterId).get());
    }

    @Override
    public List<RecruiterDTO> findUsers() {
        List<Role> roles = new ArrayList<>();
        Optional<Role> role = roleRepository.findByName(ERole.ROLE_ADMIN);
        if(role.isPresent()){
            roles.add(role.get());
        }
        return recruiterTransformer.toDTOList(recruiterRepository.findByRoleIn(roles));
    }

    @Override
    public MessageResponse delete(Long recruiterId) {
        RecruiterDTO recruiter = findById(recruiterId);
        recruiter.setDeleted(true);
        super.save(recruiter);
        return new MessageResponse("Recruiter has been deleted successfully");
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleTransformer.toDTOList(roleRepository.findAll());
    }

    @Override
    public MessageResponse restore(RecruiterDTO recruiterDTO) {
        RecruiterDTO recruiter = findById(recruiterDTO.getRecruiterId());
        recruiter.setDeleted(false);
        super.save(recruiter);
        return new MessageResponse("Recruiter has been restored successfully");
    }


}
