package com.gym.service;


import com.gym.domain.dto.InstructorDTO;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.dto.PlanRequest;
import com.gym.domain.filter.Filter;

import java.util.List;

public interface PlanService {
    List<PlanDTO> findAll(Filter...filters);
    PlanDTO findById(Integer id);
    PlanDTO save(PlanRequest planRequest);
    PlanDTO update(PlanDTO planDTO);
    PlanDTO delete(PlanDTO planDTO);
    InstructorDTO findInstructorByPlanId(Integer planId);
}
