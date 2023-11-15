package com.gym.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanExerciseDTO {
    private PlanDTO planDTO;
    private ExerciseDTO exerciseDTO;
    private Integer sets;
    private Integer reps;
    private String day;
}
