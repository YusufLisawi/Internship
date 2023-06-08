package com.nttdata.beca.transformer;

import com.nttdata.beca.entity.Role;
import com.nttdata.beca.dto.RoleDTO;

public class RoleTransformer extends Transformer<Role, RoleDTO>{

    @Override
    public Role toEntity(RoleDTO dto) {
        if (dto == null) {
            return null;
        } else {
            Role role = new Role();
            role.setId(dto.getId());
            role.setName(dto.getName());
            return role;
        }
    }

    @Override
    public RoleDTO toDTO(Role entity) {
        if (entity == null) {
            return null;
        } else {
            return new RoleDTO(entity.getId(),entity.getName());
        }
    }
    
}
