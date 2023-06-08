package com.nttdata.beca.dto;

import com.nttdata.beca.entity.enums.ERole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDTO {
    
    private Integer id;
    private ERole name;
}
