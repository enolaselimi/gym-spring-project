package com.gym.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlanExerciseRequest {
    private Integer plan_id;
    private Integer exercise_id;
    private Integer sets;
    private Integer reps;
    private String day;
}
