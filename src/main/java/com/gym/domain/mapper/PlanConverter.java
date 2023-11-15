package com.gym.domain.mapper;


import com.gym.domain.dto.PlanDTO;
import com.gym.domain.dto.PlanRequest;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.Plan;


public class PlanConverter {
    public static PlanDTO toDTO (Plan plan){
        PlanDTO planDTO = new PlanDTO();
        planDTO.setId(plan.getId());
        planDTO.setName(plan.getName());
        planDTO.setInstructorDTO(InstructorConverter.toDTO(plan.getInstructor()));
        planDTO.setDifficulty(plan.getDifficulty());
        return planDTO;
    }

    public static Plan toEntity (PlanRequest planRequest, Instructor instructor){
        Plan plan = new Plan();
        plan.setName(planRequest.getName());
        plan.setInstructor(instructor);
        plan.setDifficulty(planRequest.getDifficulty());
        return plan;
    }

    public static Plan fromDTOtoEntity(PlanDTO planDTO){
        Plan plan = new Plan();
        plan.setId(planDTO.getId());
        plan.setName(planDTO.getName());
        plan.setInstructor(InstructorConverter.fromDTOtoEntity(planDTO.getInstructorDTO()));
        plan.setDifficulty(planDTO.getDifficulty());
        return plan;
    }
}
