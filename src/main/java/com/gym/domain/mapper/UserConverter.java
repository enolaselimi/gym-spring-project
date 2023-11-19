package com.gym.domain.mapper;

import com.gym.domain.dto.RoleDTO;
import com.gym.domain.dto.UserDTO;
import com.gym.domain.entity.Role;
import com.gym.domain.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserConverter {
    public static UserDTO toDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUser_id(user.getUserId());
        userDTO.setUsername(user.getUsername());
        Set<RoleDTO> authorities = user.getAuthorities().stream()
                                    .map(a -> RoleConverter.toDTO((Role) a))
                                    .collect(Collectors.toSet());
        userDTO.setAuthorities(authorities);
        return userDTO;
    }
}
