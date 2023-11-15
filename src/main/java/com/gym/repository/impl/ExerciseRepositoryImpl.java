package com.gym.repository.impl;

import com.gym.domain.entity.Exercise;
import com.gym.domain.entity.Plan;
import com.gym.domain.filter.Filter;
import com.gym.repository.ExerciseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ExerciseRepositoryImpl implements ExerciseRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private static final String FIND_QUERY = "SELECT e FROM Exercise e WHERE e.id = :id";
    @Override
    public List<Exercise> findAll(Filter...filters) {
        String FIND_ALL_QUERY = "SELECT e FROM Exercise e WHERE 1=1 ";
        if(filters == null){
            return entityManager.createQuery(FIND_ALL_QUERY, Exercise.class)
                    .getResultList();
        }
        if(filters[0].getValue() != null){
            FIND_ALL_QUERY += "AND e."+filters[0].getField()+" "+filters[0].getOperator()+" '%"+filters[0].getValue()+"%' ";
        }
        if(filters[0].getSort() != null){
            FIND_ALL_QUERY += "ORDER BY e."+filters[0].getField()+" "+filters[0].getSort();
        }
        if(filters[0].getPageNumber() != null && filters[0].getPageSize() != null){
            return entityManager.createQuery(FIND_ALL_QUERY, Exercise.class)
                    .setFirstResult((filters[0].getPageNumber()-1)*filters[0].getPageSize())
                    .setMaxResults(filters[0].getPageSize())
                    .getResultList();
        }
        return entityManager.createQuery(FIND_ALL_QUERY, Exercise.class)
                .getResultList();
    }

    @Override
    public Exercise findById(Integer id) {
        return entityManager.createQuery(FIND_QUERY, Exercise.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Exercise save(Exercise exercise) {
        entityManager.persist(exercise);
        return exercise;
    }

    @Override
    public Exercise update(Exercise exercise) {
        return entityManager.merge(exercise);
    }

    @Override
    public Exercise delete(Exercise exercise) {
        entityManager.remove(entityManager.merge(exercise));
        return exercise;
    }

    @Override
    public List<Plan> findAllPlansByGivenExercise(Integer exerciseId,Filter...filters) {
        String FIND_ALL_PLANS_BY_EXERCISE = "SELECT DISTINCT p FROM Plan p " +
                "JOIN PlanExercise pe ON pe.plan = p " +
                "WHERE pe.exercise.id = :exerciseId ";
        if(filters == null){
            return entityManager.createQuery(FIND_ALL_PLANS_BY_EXERCISE, Plan.class)
                    .getResultList();
        }
        if(filters[0].getValue() != null){
            FIND_ALL_PLANS_BY_EXERCISE += "AND p."+filters[0].getField()+" "+filters[0].getOperator()+" '%"+filters[0].getValue()+"%' ";
        }
        if(filters[0].getSort() != null){
            FIND_ALL_PLANS_BY_EXERCISE += "ORDER BY p."+filters[0].getField()+" "+filters[0].getSort();
        }
        if(filters[0].getPageNumber() != null && filters[0].getPageSize() != null){
            return entityManager.createQuery(FIND_ALL_PLANS_BY_EXERCISE, Plan.class)
                    .setFirstResult((filters[0].getPageNumber()-1)*filters[0].getPageSize())
                    .setMaxResults(filters[0].getPageSize())
                    .setParameter("exerciseId",exerciseId)
                    .getResultList();
        }
        return entityManager.createQuery(FIND_ALL_PLANS_BY_EXERCISE, Plan.class)
                .setParameter("exerciseId", exerciseId)
                .getResultList();
    }
}
