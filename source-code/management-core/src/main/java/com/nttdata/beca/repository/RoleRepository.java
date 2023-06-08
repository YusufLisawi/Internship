package com.nttdata.beca.repository;

import java.util.Optional;

import com.nttdata.beca.entity.Role;
import com.nttdata.beca.entity.enums.ERole;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{
    Optional<Role> findByName(ERole name);

}
