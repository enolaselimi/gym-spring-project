package com.gym.repository;

import com.gym.domain.dto.ClientDTO;
import com.gym.domain.entity.Client;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.Plan;
import com.gym.domain.filter.Filter;

import java.util.List;

public interface InstructorRepository {
    List<Instructor> findAll(Filter...filters);
    Instructor findById(Integer id);
    Instructor save(Instructor instructor);
    Instructor update(Instructor instructor);
    Instructor delete(Instructor instructor);
    List<Plan> findAllPlansByInstructorId(Integer instructorId, Filter...filters);
    List<Client> findAllClientsByInstructorId(Integer instructorId, Filter...filters);
}
