package com.gym.domain.mapper;

import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.dto.ExerciseRequest;
import com.gym.domain.entity.Exercise;

public class ExerciseConverter {

    public static ExerciseDTO toDTO(Exercise exercise){
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setId(exercise.getId());
        exerciseDTO.setName(exercise.getName());
        exerciseDTO.setDifficulty(exercise.getDifficulty());
        return exerciseDTO;
    }

    public static Exercise toEntity(ExerciseRequest exerciseRequest){
        Exercise exercise = new Exercise();
        exercise.setName(exerciseRequest.getName());
        exercise.setDifficulty(exerciseRequest.getDifficulty());
        return exercise;
    }

    public static Exercise fromDTOtoEntity(ExerciseDTO exerciseDTO){
        Exercise exercise = new Exercise();
        exercise.setId(exerciseDTO.getId());
        exercise.setName(exerciseDTO.getName());
        exercise.setDifficulty(exerciseDTO.getDifficulty());
        return exercise;
    }
}
