package com.ecommerce.respository;

import com.ecommerce.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductCriteria {

    private final EntityManager entityManager;

    public ProductCriteria(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Product> filterProducts(Integer categoryId, Integer minPrice, Integer maxPrice, String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        // 🟢 Apply filters only if the parameter is provided

        if (categoryId != null) {
            predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
        }

        if (minPrice != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
        }

        // Apply only available predicates
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Product> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}

