package com.example.storage.repository;

import com.example.storage.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FileRepositoryCustomImpl implements FileRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<File> findFiles(String name, LocalDateTime startDate, LocalDateTime endDate, List<String> types) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<File> criteriaQuery = criteriaBuilder.createQuery(File.class);

        Root<File> fileRoot = criteriaQuery.from(File.class);
        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(criteriaBuilder.like(fileRoot.get("name"), "%" + name + "%"));
        }
        if (startDate != null && endDate != null) {
            predicates.add(criteriaBuilder.between(fileRoot.get("dateModification"), startDate, endDate));
        }

        if (types != null && !types.isEmpty()) {
            CriteriaBuilder.In<String> stringIn = criteriaBuilder.in(fileRoot.get("type"));
            types.forEach(stringIn::value);
            predicates.add(stringIn);
        }

        Predicate[] predicatesArray = predicates.toArray(new Predicate[0]);
        criteriaQuery.select(fileRoot).where(predicatesArray);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
