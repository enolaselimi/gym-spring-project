package com.gym.service;

import com.gym.domain.dto.ClientDTO;
import com.gym.domain.dto.ClientRequest;
import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.filter.Filter;

import java.util.List;

public interface ClientService {
    List<ClientDTO> findAll(Filter...filters);
    ClientDTO findById(Integer id);
    ClientDTO save(ClientRequest clientRequest);
    ClientDTO update(ClientDTO clientDTO);
    ClientDTO delete(ClientDTO clientDTO);
    PlanDTO findPlanByClientId(Integer clientId);
    List<ExerciseDTO> findAllExercises(Integer clientId,Filter...filters);
}
