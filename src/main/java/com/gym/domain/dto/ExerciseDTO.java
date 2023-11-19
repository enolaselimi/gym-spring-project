package com.gym.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gym.domain.entity.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDTO {
    @JsonIgnore
    private Integer id;
    @NotBlank(message = "Exercise name cannot be empty.")
    private String name;
    @NotNull(message = "Difficulty cannot be null")
    private Difficulty difficulty;
}
