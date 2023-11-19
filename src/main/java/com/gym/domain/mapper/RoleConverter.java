package com.gym.domain.mapper;

import com.gym.domain.dto.RoleDTO;
import com.gym.domain.entity.Role;

public class RoleConverter {

    public static RoleDTO toDTO(Role role){
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRole_id(role.getRoleId());
        roleDTO.setAuthority(role.getAuthority());
        return roleDTO;
    }
}
