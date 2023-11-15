package com.gym.service;

import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.dto.PlanExerciseDTO;
import com.gym.domain.dto.PlanExerciseRequest;
import com.gym.domain.filter.Filter;

import java.util.List;

public interface PlanExerciseService {
    List<PlanExerciseDTO> findAll();
    PlanExerciseDTO findById(Integer planId, Integer exerciseId);
    PlanExerciseDTO save(PlanExerciseRequest planExerciseRequest);
    PlanExerciseDTO update(PlanExerciseDTO planExerciseDTO);
    PlanExerciseDTO delete(PlanExerciseDTO planExerciseDTO);
    List<PlanExerciseDTO> findPlanExercises(Integer planId, Filter...filters);
}
