package com.gym.repository.impl;

import com.gym.domain.entity.Client;
import com.gym.domain.entity.Exercise;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.Plan;
import com.gym.domain.filter.Filter;
import com.gym.repository.PlanRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlanRepositoryImpl implements PlanRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_QUERY = "SELECT p FROM Plan p WHERE p.id = :id";
    private static final String FIND_INSTRUCTOR_BY_PLAN = "SELECT i FROM Instructor i " +
            "JOIN Plan p ON p.instructor = i " +
            "WHERE p.id = :id";

    @Override
    public List<Plan> findAll(Filter...filters) {
        String FIND_ALL_QUERY = "SELECT p FROM Plan p WHERE 1=1 ";
        if(filters == null){
            return entityManager.createQuery(FIND_ALL_QUERY, Plan.class)
                    .getResultList();
        }
        if(filters[0].getValue() != null){
            FIND_ALL_QUERY += "AND p."+filters[0].getField()+" "+filters[0].getOperator()+" '%"+filters[0].getValue()+"%' ";
        }

        if(filters[1].getValue() != null){
            FIND_ALL_QUERY += "AND p."+filters[1].getField()+" "+filters[1].getOperator()+" '%"+filters[1].getValue()+"%' ";
        }

        if(filters[0].getSort() != null){
            FIND_ALL_QUERY += "ORDER BY p."+filters[0].getField()+" "+filters[0].getSort();
        }

        if(filters[1].getSort() != null){
            FIND_ALL_QUERY += "ORDER BY p."+filters[1].getField()+" "+filters[1].getSort();
        }

        if(filters[0].getPageSize() != null && filters[0].getPageNumber() != null){
            return entityManager.createQuery(FIND_ALL_QUERY, Plan.class)
                    .setFirstResult((filters[0].getPageNumber()-1)*filters[0].getPageSize())
                    .setMaxResults(filters[0].getPageSize())
                    .getResultList();
        }

        return entityManager.createQuery(FIND_ALL_QUERY, Plan.class)
                .getResultList();
    }

    @Override
    public Plan findById(Integer id) {
        return entityManager.createQuery(FIND_QUERY, Plan.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Plan save(Plan plan) {
        entityManager.persist(plan);
        return plan;
    }

    @Override
    public Plan update(Plan plan) {
        return entityManager.merge(plan);
    }

    @Override
    public Plan delete(Plan plan) {
        entityManager.remove(entityManager.merge(plan));
        return plan;
    }

    @Override
    public Instructor findInstructorByPlanId(Integer planId) {
        return entityManager.createQuery(FIND_INSTRUCTOR_BY_PLAN, Instructor.class)
                .setParameter("id", planId)
                .getSingleResult();
    }
}
