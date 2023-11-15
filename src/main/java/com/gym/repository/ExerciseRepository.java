package com.gym.repository;

import com.gym.domain.entity.Exercise;
import com.gym.domain.entity.Plan;
import com.gym.domain.filter.Filter;
import java.util.List;

public interface ExerciseRepository {
    List<Exercise> findAll(Filter...filters);
    Exercise findById(Integer id);
    Exercise save(Exercise exercise);
    Exercise update(Exercise exercise);
    Exercise delete(Exercise exercise);
    List<Plan> findAllPlansByGivenExercise(Integer exerciseId,Filter...filters);
}
