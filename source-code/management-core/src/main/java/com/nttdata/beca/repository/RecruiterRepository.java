package com.nttdata.beca.repository;

import java.util.List;

import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.entity.Role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterRepository extends CrudRepository<Recruiter, Long> {
    List<Recruiter> findByDeletedFalse();
    List<Recruiter> findByDeletedTrue();
    Recruiter findRecruiterByRecruiterId(Long id);

    Recruiter findByUsername(String username);
    Recruiter findByEmail(String email);
    List<Recruiter> findByRoleIn(List<Role> role);
    Boolean existsByEmail(String email);
}
