package com.gym.service.impl;

import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.dto.ExerciseRequest;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.ExerciseConverter;
import com.gym.domain.mapper.PlanConverter;
import com.gym.repository.ExerciseRepository;
import com.gym.service.ExerciseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import static com.gym.domain.mapper.ExerciseConverter.toDTO;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    @Autowired
    private ExerciseRepository exerciseRepository;

    @Override
    public List<ExerciseDTO> findAll(Filter...filters) {
        return exerciseRepository.findAll(filters)
                .stream()
                .map(ExerciseConverter::toDTO)
                .toList();
    }

    @Override
    public ExerciseDTO findById(Integer id) {
        return toDTO(exerciseRepository.findById(id));
    }

    @Transactional
    @Override
    public ExerciseDTO save(ExerciseRequest exerciseRequest) {
        return toDTO(exerciseRepository.save(ExerciseConverter.toEntity(exerciseRequest)));
    }

    @Transactional
    @Override
    public ExerciseDTO update(ExerciseDTO exerciseDTO) {
        return toDTO(exerciseRepository.update(ExerciseConverter.fromDTOtoEntity(exerciseDTO)));
    }

    @Transactional
    @Override
    public ExerciseDTO delete(ExerciseDTO exerciseDTO) {
        return toDTO(exerciseRepository.delete(ExerciseConverter.fromDTOtoEntity(exerciseDTO)));
    }

    @Override
    public List<PlanDTO> findPlansByGivenExercise(Integer exerciseId, Filter...filters) {
        return exerciseRepository.findAllPlansByGivenExercise(exerciseId,filters)
                .stream()
                .map(PlanConverter::toDTO)
                .toList();
    }
}
