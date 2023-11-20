package com.gym.repository.impl;

import com.gym.domain.entity.Client;
import com.gym.domain.entity.Exercise;
import com.gym.domain.entity.Plan;
import com.gym.domain.filter.Filter;
import com.gym.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientRepositoryImpl implements ClientRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private static final String FIND_QUERY = "SELECT c FROM Client c WHERE c.id = :id";

    private static final String FIND_PLAN_BY_CLIENT = "SELECT p FROM Plan p JOIN Client c ON c.plan = p WHERE c.id = :clientId";

    @Override
    public List<Client> findAll(Filter...filters) {
        String FIND_ALL_QUERY = "SELECT c FROM Client c JOIN c.plan p WHERE 1=1 ";
        if(filters == null){
            return entityManager.createQuery(FIND_ALL_QUERY, Client.class)
                    .getResultList();
        }
        if(filters[0].getValue() != null){
            FIND_ALL_QUERY += "AND c."+filters[0].getField()+" "+filters[0].getOperator()+" '%"+filters[0].getValue()+"%' ";
        }
        if(filters[1].getValue() != null){

            FIND_ALL_QUERY += "AND p."+filters[1].getField()+" "+filters[1].getOperator()+" "+filters[1].getValue()+" ";
        }

        if(filters[0].getSort() != null){
            FIND_ALL_QUERY += "ORDER BY c."+filters[0].getField()+" "+filters[0].getSort();
        } else if (filters[1].getSort() != null) {
            FIND_ALL_QUERY += "ORDER BY p."+filters[1].getField()+" "+filters[1].getSort();
        }

        if(filters[0].getPageSize() != null && filters[0].getPageNumber() != null){
            return entityManager.createQuery(FIND_ALL_QUERY, Client.class)
                    .setFirstResult((filters[0].getPageNumber()-1)*filters[0].getPageSize())
                    .setMaxResults(filters[0].getPageSize())
                    .getResultList();
        }
        return entityManager.createQuery(FIND_ALL_QUERY, Client.class)
                .getResultList();
    }

    @Override
    public Client findById(Integer id) {
        return entityManager.createQuery(FIND_QUERY,Client.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Client save(Client client) {
        entityManager.persist(client);
        return client;
    }

    @Override
    public Client update(Client client) {
        return entityManager.merge(client);
    }

    @Override
    public Client delete(Client client) {
        entityManager.remove(entityManager.merge(client));
        return client;
    }

    @Override
    public Plan findPlanByClientId(Integer clientId) {
        return entityManager.createQuery(FIND_PLAN_BY_CLIENT, Plan.class)
                .setParameter("clientId", clientId)
                .getSingleResult();
    }

    @Override
    public List<Exercise> findAllExercises(Integer clientId,Filter...filters) {
        String FIND_ALL_EXERCISES = "SELECT e FROM Exercise e " +
                "INNER JOIN PlanExercise pe ON pe.exercise = e " +
                "INNER JOIN Client c ON c.plan = pe.plan WHERE c.id = :clientId ";

        if(filters == null){
            return entityManager.createQuery(FIND_ALL_EXERCISES, Exercise.class)
                    .getResultList();
        }
        if(filters[0].getValue() != null){
            FIND_ALL_EXERCISES += "AND e."+filters[0].getField()+" "+filters[0].getOperator()+" '%"+filters[0].getValue()+"%' ";
        }

        if(filters[1].getValue() != null){
            FIND_ALL_EXERCISES += "AND pe."+filters[1].getField()+" "+filters[1].getOperator()+" '%"+filters[1].getValue()+"%' ";
        }

        if(filters[0].getSort() != null) {
            FIND_ALL_EXERCISES += "ORDER BY e." + filters[0].getField() + " " + filters[0].getSort();
        }

        if(filters[1].getSort() != null) {
            FIND_ALL_EXERCISES += "ORDER BY pe." + filters[1].getField() + " " + filters[1].getSort();
        }

        if(filters[0].getPageSize() != null && filters[0].getPageNumber() != null){
            return entityManager.createQuery(FIND_ALL_EXERCISES, Exercise.class)
                    .setFirstResult((filters[0].getPageNumber() - 1) * filters[0].getPageSize())
                    .setMaxResults(filters[0].getPageSize())
                    .setParameter("clientId", clientId)
                    .getResultList();
        }
        return entityManager.createQuery(FIND_ALL_EXERCISES, Exercise.class)
                .setParameter("clientId", clientId)
                .getResultList();
    }
}
