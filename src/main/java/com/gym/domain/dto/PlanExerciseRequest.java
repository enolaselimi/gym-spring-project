package com.gym.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlanExerciseRequest {
    @NotNull(message = "Plan cannot be blank")
    private Integer plan_id;
    @NotNull(message = "Exercise cannot be blank")
    private Integer exercise_id;
    @NotNull(message = "Sets cannot be blank")
    @Positive(message = "Sets should be a positive number")
    private Integer sets;
    @NotNull(message = "Reps cannot be blank")
    @Positive(message = "Reps should be a positive number")
    private Integer reps;
    @NotBlank(message = "Day cannot be blank")
    private String day;
}
