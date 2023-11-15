package com.gym.repository;
import com.gym.domain.entity.Exercise;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.Plan;
import com.gym.domain.filter.Filter;

import java.util.List;

public interface PlanRepository {
    List<Plan> findAll(Filter...filters);
    Plan findById(Integer id);
    Plan save(Plan plan);
    Plan update(Plan plan);
    Plan delete(Plan plan);
    Instructor findInstructorByPlanId(Integer planId);

}
