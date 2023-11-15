package com.gym.domain.mapper;

import com.gym.domain.dto.PlanExerciseDTO;
import com.gym.domain.dto.PlanExerciseRequest;
import com.gym.domain.entity.Exercise;
import com.gym.domain.entity.Plan;
import com.gym.domain.entity.PlanExercise;

public class PlanExerciseConverter {

    public static PlanExerciseDTO toDTO(PlanExercise planExercise){
        PlanExerciseDTO planExerciseDTO = new PlanExerciseDTO();
        planExerciseDTO.setPlanDTO(PlanConverter.toDTO(planExercise.getPlan()));
        planExerciseDTO.setExerciseDTO(ExerciseConverter.toDTO(planExercise.getExercise()));
        planExerciseDTO.setSets(planExercise.getSets());
        planExerciseDTO.setReps(planExercise.getReps());
        planExerciseDTO.setDay(planExercise.getDay());
        return planExerciseDTO;
    }

    public static PlanExercise toEntity(PlanExerciseRequest planExerciseRequest, Plan plan, Exercise exercise){
        PlanExercise planExercise = new PlanExercise();
        planExercise.setPlan(plan);
        planExercise.setExercise(exercise);
        planExercise.setSets(planExerciseRequest.getSets());
        planExercise.setReps(planExerciseRequest.getReps());
        planExercise.setDay(planExerciseRequest.getDay());
        return planExercise;
    }

    public static PlanExercise fromDTOtoEntity(PlanExerciseDTO planExerciseDTO){
        PlanExercise planExercise = new PlanExercise();
        planExercise.setSets(planExerciseDTO.getSets());
        planExercise.setReps(planExerciseDTO.getReps());
        planExercise.setDay(planExerciseDTO.getDay());
        planExercise.setPlan(PlanConverter.fromDTOtoEntity(planExerciseDTO.getPlanDTO()));
        planExercise.setExercise(ExerciseConverter.fromDTOtoEntity(planExerciseDTO.getExerciseDTO()));
        return planExercise;
    }
}
