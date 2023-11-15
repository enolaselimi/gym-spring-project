package com.gym.service;


import com.gym.domain.dto.ClientDTO;
import com.gym.domain.dto.InstructorDTO;
import com.gym.domain.dto.InstructorRequest;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.filter.Filter;

import java.util.List;

public interface InstructorService {
    List<InstructorDTO> findAll(Filter...filters);
    InstructorDTO findById(Integer id);
    InstructorDTO save(InstructorRequest instructorRequest);
    InstructorDTO update(InstructorDTO instructorDTO);
    InstructorDTO delete(InstructorDTO instructorDTO);
    List<PlanDTO> findAllPlansByInstructorId(Integer instructorId, Filter...filters);
    List<ClientDTO> findAllClientsByInstructorId(Integer instructorId, Filter...filters);
}
