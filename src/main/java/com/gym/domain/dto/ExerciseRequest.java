package com.gym.domain.dto;

import com.gym.domain.entity.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExerciseRequest {
    @NotBlank(message = "Exercise name cannot be empty.")
    private String name;
    @NotNull(message = "Difficulty cannot be null")
    private Difficulty difficulty;
}
