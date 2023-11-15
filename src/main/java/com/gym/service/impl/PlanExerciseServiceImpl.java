package com.gym.service.impl;

import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.dto.PlanExerciseDTO;
import com.gym.domain.dto.PlanExerciseRequest;
import com.gym.domain.entity.Exercise;
import com.gym.domain.entity.Plan;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.ExerciseConverter;
import com.gym.domain.mapper.PlanConverter;
import com.gym.domain.mapper.PlanExerciseConverter;
import com.gym.repository.PlanExerciseRepository;
import com.gym.service.ExerciseService;
import com.gym.service.PlanExerciseService;
import com.gym.service.PlanService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gym.domain.mapper.PlanExerciseConverter.toDTO;

@Service
public class PlanExerciseServiceImpl implements PlanExerciseService {
    @Autowired
    private PlanExerciseRepository planExerciseRepository;
    @Autowired
    private PlanService planService;
    @Autowired
    private ExerciseService exerciseService;

    @Override
    public List<PlanExerciseDTO> findAll() {
        return planExerciseRepository.findAll()
                .stream()
                .map(PlanExerciseConverter::toDTO)
                .toList();
    }

    @Override
    public PlanExerciseDTO findById(Integer planId, Integer exerciseId) {
        return toDTO(planExerciseRepository.findById(planId,exerciseId));
    }

    @Transactional
    @Override
    public PlanExerciseDTO save(PlanExerciseRequest planExerciseRequest) {
        Plan plan = PlanConverter.fromDTOtoEntity(planService.findById(planExerciseRequest.getPlan_id()));
        Exercise exercise = ExerciseConverter.fromDTOtoEntity(exerciseService.findById(planExerciseRequest.getExercise_id()));
        return toDTO(planExerciseRepository.save(PlanExerciseConverter.toEntity(planExerciseRequest, plan ,exercise)));
    }

    @Transactional
    @Override
    public PlanExerciseDTO update(PlanExerciseDTO planExerciseDTO) {
        return toDTO(planExerciseRepository.update(PlanExerciseConverter.fromDTOtoEntity(planExerciseDTO)));
    }

    @Transactional
    @Override
    public PlanExerciseDTO delete(PlanExerciseDTO planExerciseDTO) {
        return toDTO(planExerciseRepository.delete(PlanExerciseConverter.fromDTOtoEntity(planExerciseDTO)));
    }

    @Override
    public List<PlanExerciseDTO> findPlanExercises(Integer planId, Filter...filters) {
        return planExerciseRepository.findPlanExercises(planId, filters)
                .stream()
                .map(PlanExerciseConverter::toDTO)
                .toList();
    }
}
