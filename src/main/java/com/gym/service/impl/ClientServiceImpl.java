package com.gym.service.impl;

import com.gym.domain.dto.ClientDTO;
import com.gym.domain.dto.ClientRequest;
import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.dto.PlanDTO;
import com.gym.domain.entity.Client;
import com.gym.domain.filter.Filter;
import com.gym.domain.mapper.ClientConverter;
import com.gym.domain.mapper.ExerciseConverter;
import com.gym.domain.mapper.PlanConverter;
import com.gym.repository.ClientRepository;
import com.gym.service.ClientService;
import com.gym.service.PlanService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gym.domain.mapper.ClientConverter.toDTO;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PlanService planService;

    @Override
    public List<ClientDTO> findAll(Filter...filters) {
        return clientRepository.findAll(filters)
                .stream()
                .map(ClientConverter::toDTO)
                .toList();
    }

    @Override
    public ClientDTO findById(Integer id) {
        return toDTO(clientRepository.findById(id));
    }

    @Transactional
    @Override
    public ClientDTO save(ClientRequest clientRequest) {
        PlanDTO clientPlan = planService.findById(clientRequest.getPlan_id());
        Client clientToAdd = ClientConverter.toEntity(clientRequest, PlanConverter.fromDTOtoEntity(clientPlan));
        return toDTO(clientRepository.save(clientToAdd));
    }

    @Transactional
    @Override
    public ClientDTO update(ClientDTO clientDTO) {
        return toDTO(clientRepository.update(ClientConverter.fromDTOtoEntity(clientDTO)));
    }

    @Transactional
    @Override
    public ClientDTO delete(ClientDTO clientDTO) {
        return toDTO(clientRepository.delete(ClientConverter.fromDTOtoEntity(clientDTO)));
    }

    @Override
    public PlanDTO findPlanByClientId(Integer clientId) {
        return PlanConverter.toDTO(clientRepository.findPlanByClientId(clientId));
    }

    @Override
    public List<ExerciseDTO> findAllExercises(Integer clientId, Filter nameFilter) {
        return clientRepository.findAllExercises(clientId,nameFilter)
                .stream()
                .map(ExerciseConverter::toDTO)
                .toList();
    }
}
