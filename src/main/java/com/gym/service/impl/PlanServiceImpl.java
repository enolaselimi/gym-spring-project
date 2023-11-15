package com.gym.service.impl;

import com.gym.domain.dto.InstructorDTO;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.dto.PlanRequest;
import com.gym.domain.entity.Instructor;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.InstructorConverter;
import com.gym.domain.mapper.PlanConverter;
import com.gym.repository.PlanRepository;
import com.gym.service.InstructorService;
import com.gym.service.PlanService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gym.domain.mapper.PlanConverter.toDTO;

@Service
public class PlanServiceImpl implements PlanService {
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private InstructorService instructorService;

    @Override
    public List<PlanDTO> findAll(Filter...filters) {
        return planRepository.findAll(filters)
                .stream()
                .map(PlanConverter::toDTO)
                .toList();
    }

    @Override
    public PlanDTO findById(Integer id) {
        return toDTO(planRepository.findById(id));
    }

    @Transactional
    @Override
    public PlanDTO save(PlanRequest planRequest) {
        InstructorDTO instructorDTO = instructorService.findById(planRequest.getInstructor_id());
        Instructor instructor = InstructorConverter.fromDTOtoEntity(instructorDTO);
        return toDTO(planRepository.save(PlanConverter.toEntity(planRequest, instructor)));
    }

    @Transactional
    @Override
    public PlanDTO update(PlanDTO planDTO) {
        return toDTO(planRepository.update(PlanConverter.fromDTOtoEntity(planDTO)));
    }

    @Transactional
    @Override
    public PlanDTO delete(PlanDTO planDTO) {
        return toDTO(planRepository.delete(PlanConverter.fromDTOtoEntity(planDTO)));
    }

    @Override
    public InstructorDTO findInstructorByPlanId(Integer planId) {
        return InstructorConverter.toDTO(planRepository.findInstructorByPlanId(planId));
    }


}
