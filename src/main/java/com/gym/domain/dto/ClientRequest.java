package com.gym.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientRequest {
    @NotBlank(message = "Name cannot be empty.")
    @Size(min = 2, message = "Name cannot be less than 2 characters.")
    private String name;
    @Email(message = "Invalid email format")
    private String email;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format")
    private String dateOfBirth;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format")
    private String dateJoined;
    @Positive(message = "Invalid weight.")
    private Float weight;
    @Positive(message = "Invalid height.")
    private Float height;
    @NotNull(message = "Client must be subscribed to a plan.")
    @Positive(message = "Plan id must be a positive integer.")
    private Integer plan_id;
}
