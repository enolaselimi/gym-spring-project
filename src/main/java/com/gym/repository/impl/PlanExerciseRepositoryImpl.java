package com.gym.repository.impl;

import com.gym.domain.entity.Plan;
import com.gym.domain.entity.PlanExercise;
import com.gym.domain.filter.Filter;
import com.gym.repository.PlanExerciseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PlanExerciseRepositoryImpl implements PlanExerciseRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private static final String FIND_QUERY = "SELECT pe FROM PlanExercise pe " +
            "WHERE pe.plan.id = :planId AND pe.exercise.id = :exerciseId";
    private static final String FIND_ALL_QUERY = "SELECT pe FROM PlanExercise pe";
    @Override
    public List<PlanExercise> findAll() {
        return entityManager.createQuery(FIND_ALL_QUERY, PlanExercise.class)
                .getResultList();
    }

    @Override
    public PlanExercise findById(Integer planId, Integer exerciseId) {
        return entityManager.createQuery(FIND_QUERY, PlanExercise.class)
                .setParameter("planId", planId)
                .setParameter("exerciseId", exerciseId)
                .getSingleResult();
    }

    @Override
    public PlanExercise save(PlanExercise planExercise) {
        entityManager.persist(planExercise);
        return planExercise;
    }

    @Override
    public PlanExercise update(PlanExercise planExercise) {
        return entityManager.merge(planExercise);
    }

    @Override
    public PlanExercise delete(PlanExercise planExercise) {
        entityManager.remove(entityManager.merge(planExercise));
        return planExercise;
    }

    @Override
    public List<PlanExercise> findPlanExercises(Integer planId, Filter...filters) {
        String FIND_ALL_EXERCISES_BY_PLAN = "SELECT pe FROM PlanExercise pe " +
                "WHERE pe.plan.id = :planId ";
        if(filters == null){
            return entityManager.createQuery(FIND_ALL_EXERCISES_BY_PLAN, PlanExercise.class)
                    .getResultList();
        }
        if(filters[0].getValue() != null){
            FIND_ALL_EXERCISES_BY_PLAN += "AND pe."+filters[0].getField()+" "+filters[0].getOperator()+" '%"+filters[0].getValue()+"%' ";
        }
        if(filters[1].getValue() != null){
            FIND_ALL_EXERCISES_BY_PLAN += "AND pe."+filters[1].getField()+" "+filters[1].getOperator()+" "+filters[1].getValue()+" ";
        }
        if(filters[2].getValue() != null){
            FIND_ALL_EXERCISES_BY_PLAN += "AND pe."+filters[2].getField()+" "+filters[2].getOperator()+" "+filters[2].getValue()+" ";
        }

        if(filters[0].getSort() != null){
            FIND_ALL_EXERCISES_BY_PLAN += "ORDER BY pe."+filters[0].getField()+" "+filters[0].getSort();
        }
        if(filters[1].getSort() != null){
            FIND_ALL_EXERCISES_BY_PLAN += "ORDER BY pe."+filters[1].getField()+" "+filters[1].getSort();
        }
        if(filters[2].getSort() != null){
            FIND_ALL_EXERCISES_BY_PLAN += "ORDER BY pe."+filters[2].getField()+" "+filters[2].getSort();
        }

        if(filters[0].getPageSize() != null && filters[0].getPageNumber() != null){
            return entityManager.createQuery(FIND_ALL_EXERCISES_BY_PLAN, PlanExercise.class)
                    .setFirstResult((filters[0].getPageNumber()-1)*filters[0].getPageSize())
                    .setMaxResults(filters[0].getPageSize())
                    .setParameter("planId",planId)
                    .getResultList();
        }
        return entityManager.createQuery(FIND_ALL_EXERCISES_BY_PLAN, PlanExercise.class)
                .setParameter("planId", planId)
                .getResultList();
    }
}
