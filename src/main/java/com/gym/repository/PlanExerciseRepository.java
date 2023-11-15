package com.gym.repository;
import com.gym.domain.entity.Exercise;
import com.gym.domain.entity.PlanExercise;
import com.gym.domain.filter.Filter;

import java.util.List;

public interface PlanExerciseRepository {
    List<PlanExercise> findAll();
    PlanExercise findById(Integer planId, Integer exerciseId);
    PlanExercise save(PlanExercise planExercise);
    PlanExercise update(PlanExercise planExercise);
    PlanExercise delete(PlanExercise planExercise);
    List<PlanExercise> findPlanExercises(Integer planId, Filter...filters);
}
