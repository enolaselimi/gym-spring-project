package com.gym.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpDTO {
    @NotBlank(message = "Username cannot be empty.")
    private String username;
    @NotBlank(message = "Password cannot be empty.")
    private String password;
    @NotBlank(message = "Roles cannot be empty.")
    private String authority;
    @NotNull
    private Integer entity_id;
}
