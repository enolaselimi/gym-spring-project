package com.gym.repository;

import com.gym.domain.dto.ClientRequest;
import com.gym.domain.dto.ExerciseDTO;
import com.gym.domain.entity.Client;
import com.gym.domain.entity.Exercise;
import com.gym.domain.entity.Plan;
import com.gym.domain.filter.Filter;

import java.util.List;

public interface ClientRepository {
    List<Client> findAll(Filter...filters);
    Client findById(Integer id);
    Client save(Client client);
    Client update(Client client);
    Client delete(Client client);
    Plan findPlanByClientId(Integer clientId);
    List<Exercise> findAllExercises(Integer clientId, Filter...filters);
}
