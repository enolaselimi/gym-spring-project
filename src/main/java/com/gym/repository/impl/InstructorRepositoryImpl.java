package com.gym.repository.impl;

import com.gym.domain.entity.Client;
import com.gym.domain.entity.Instructor;
import com.gym.domain.entity.Plan;
import com.gym.domain.filter.Filter;
import com.gym.repository.InstructorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InstructorRepositoryImpl implements InstructorRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private static final String FIND_QUERY = "SELECT i FROM Instructor i WHERE i.id = :id";

    @Override
    public List<Instructor> findAll(Filter...filters) {
        String FIND_ALL_QUERY = "SELECT i FROM Instructor i WHERE 1=1 ";
        if(filters == null){
            return entityManager.createQuery(FIND_ALL_QUERY, Instructor.class)
                    .getResultList();
        }
        if(filters[0].getValue() != null){
            FIND_ALL_QUERY += "AND i."+filters[0].getField()+" "+filters[0].getOperator()+" '%"+filters[0].getValue()+"%' ";
        }
        if(filters[0].getSort() != null){
            FIND_ALL_QUERY += "ORDER BY i."+filters[0].getField()+" "+filters[0].getSort();
        }
        if(filters[0].getPageSize() != null && filters[0].getPageNumber() != null){
            return entityManager.createQuery(FIND_ALL_QUERY, Instructor.class)
                    .setFirstResult((filters[0].getPageNumber()-1)*filters[0].getPageSize())
                    .setMaxResults(filters[0].getPageSize())
                    .getResultList();
        }
        return entityManager.createQuery(FIND_ALL_QUERY, Instructor.class)
                .getResultList();
    }

    @Override
    public Instructor findById(Integer id) {
        return entityManager.createQuery(FIND_QUERY, Instructor.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Instructor save(Instructor instructor) {
        entityManager.persist(instructor);
        return instructor;
    }

    @Override
    public Instructor update(Instructor instructor) {
        return entityManager.merge(instructor);
    }

    @Override
    public Instructor delete(Instructor instructor) {
        entityManager.remove(entityManager.merge(instructor));
        return instructor;
    }

    @Override
    public List<Plan> findAllPlansByInstructorId(Integer instructorId, Filter... filters) {
        String FIND_ALL_FOR_GIVEN_INSTRUCTOR = "Select p FROM Plan p JOIN p.instructor i WHERE  i.id = :id ";
        if(filters[0].getValue() != null ){
            FIND_ALL_FOR_GIVEN_INSTRUCTOR += "AND p."+filters[0].getField()+" "+filters[0].getOperator()+" '%"+filters[0].getValue()+"%' ";
        }
        if(filters[0].getSort() != null){
            FIND_ALL_FOR_GIVEN_INSTRUCTOR += "ORDER BY p."+filters[0].getField()+" "+filters[0].getSort();
        }
        return entityManager.createQuery(FIND_ALL_FOR_GIVEN_INSTRUCTOR, Plan.class)
                .setParameter("id", instructorId)
                .getResultList();
    }

    @Override
    public List<Client> findAllClientsByInstructorId(Integer instructorId, Filter...filters) {
        String FIND_ALL_FOR_GIVEN_INSTRUCTOR = "Select c FROM Client c JOIN c.plan p JOIN p.instructor i WHERE  i.id = :id ";
        if(filters[0].getValue() != null ){
            FIND_ALL_FOR_GIVEN_INSTRUCTOR += "AND c."+filters[0].getField()+" "+filters[0].getOperator()+" '%"+filters[0].getValue()+"%' ";
        }
        if(filters[1].getValue() != null){
            FIND_ALL_FOR_GIVEN_INSTRUCTOR += "AND c."+filters[0].getField()+" "+filters[0].getOperator()+" "+filters[0].getValue()+" ";
        }
        if(filters[0].getSort() != null){
            FIND_ALL_FOR_GIVEN_INSTRUCTOR += "ORDER BY c."+filters[0].getField()+" "+filters[0].getSort();
        }
        if(filters[1].getSort() != null){
            FIND_ALL_FOR_GIVEN_INSTRUCTOR += "ORDER BY c."+filters[1].getField()+" "+filters[1].getSort();
        }

        if(filters[0].getPageNumber() != null && filters[0].getPageSize() != null){
            return entityManager.createQuery(FIND_ALL_FOR_GIVEN_INSTRUCTOR, Client.class)
                    .setFirstResult((filters[0].getPageNumber()-1)*filters[0].getPageSize())
                    .setMaxResults(filters[0].getPageSize())
                    .setParameter("id", instructorId)
                    .getResultList();
        }
        return entityManager.createQuery(FIND_ALL_FOR_GIVEN_INSTRUCTOR, Client.class)
                .setParameter("id", instructorId)
                .getResultList();
    }

}
