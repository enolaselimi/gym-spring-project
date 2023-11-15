package com.gym.domain.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private Integer id;
    @NotBlank(message = "Name cannot be empty.")
    @Size(min = 2, message = "Name cannot be less than 2 characters.")
    private String name;
    @Email(message = "Invalid email format")
    private String email;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format")
    private LocalDate dateOfBirth;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format")
    private LocalDate dateJoined;
    @Positive(message = "Invalid weight.")
    private Float weight;
    @Positive(message = "Invalid height.")
    private Float height;
    @NotNull(message = "Client must be subscribed to a plan.")
    private PlanDTO planDTO;
}
