package com.gym.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer user_id;
    @NotBlank(message = "Name cannot be blank")
    private String username;
    @NotNull(message = "Roles cannot be blank")
    private Set<RoleDTO> authorities;
}
