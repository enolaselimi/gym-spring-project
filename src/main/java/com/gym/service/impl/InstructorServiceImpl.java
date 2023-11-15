package com.gym.service.impl;

import com.gym.domain.dto.ClientDTO;
import com.gym.domain.dto.InstructorDTO;
import com.gym.domain.dto.InstructorRequest;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.ClientConverter;
import com.gym.domain.mapper.InstructorConverter;
import com.gym.domain.mapper.PlanConverter;
import com.gym.repository.InstructorRepository;
import com.gym.service.InstructorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gym.domain.mapper.InstructorConverter.toDTO;

@Service
public class InstructorServiceImpl implements InstructorService {
    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public List<InstructorDTO> findAll(Filter...filters) {
        return instructorRepository.findAll(filters)
                .stream()
                .map(InstructorConverter::toDTO)
                .toList();
    }

    @Override
    public InstructorDTO findById(Integer id) {
        return toDTO(instructorRepository.findById(id));
    }

    @Transactional
    @Override
    public InstructorDTO save(InstructorRequest instructorRequest) {
        return toDTO(instructorRepository.save(InstructorConverter.toEntity(instructorRequest)));
    }

    @Transactional
    @Override
    public InstructorDTO update(InstructorDTO instructorDTO) {
        return toDTO(instructorRepository.update(InstructorConverter.fromDTOtoEntity(instructorDTO)));
    }

    @Transactional
    @Override
    public InstructorDTO delete(InstructorDTO instructorDTO) {
        return toDTO(instructorRepository.delete(InstructorConverter.fromDTOtoEntity(instructorDTO)));
    }

    @Override
    public List<PlanDTO> findAllPlansByInstructorId(Integer instructorId, Filter...filters) {
        return instructorRepository.findAllPlansByInstructorId(instructorId, filters)
                .stream()
                .map(PlanConverter::toDTO)
                .toList();
    }

    @Override
    public List<ClientDTO> findAllClientsByInstructorId(Integer instructorId, Filter...filters) {
        return instructorRepository.findAllClientsByInstructorId(instructorId, filters)
                .stream()
                .map(ClientConverter::toDTO)
                .toList();
    }
}
