package com.gym.service;


import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.dto.ExerciseRequest;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.filter.Filter;

import java.util.List;

public interface ExerciseService {
    List<ExerciseDTO> findAll(Filter...filters);
    ExerciseDTO findById(Integer id);
    ExerciseDTO save(ExerciseRequest exerciseRequest);
    ExerciseDTO update(ExerciseDTO exerciseDTO);
    ExerciseDTO delete(ExerciseDTO exerciseDTO);
    List<PlanDTO> findPlansByGivenExercise(Integer exerciseId, Filter...filters);
}
