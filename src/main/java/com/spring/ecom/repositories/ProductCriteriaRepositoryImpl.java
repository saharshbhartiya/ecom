package com.spring.ecom.repositories;

import com.spring.ecom.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Repository
public class ProductCriteriaRepositoryImpl implements ProductCriteriaRepository{


    private final EntityManager entityManager;

    @Override
    public List<Product> findProductsByCriteria(String name , BigDecimal minPrice , BigDecimal maxPrice){
        CriteriaBuilder cb =  entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq =  cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        root.fetch("category" , JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<>();

        if(name != null){
            predicates.add(cb.like(root.get("name") , "% " + name + "%"));
        }
        if(minPrice != null){
            predicates.add(cb.greaterThanOrEqualTo(root.get("price") , minPrice));
        }
        if(maxPrice != null){
            predicates.add(cb.lessThanOrEqualTo(root.get("price") , maxPrice));
        }

        cq.select(root)
                .where(predicates.toArray(new Predicate[predicates.size()]));
        return entityManager.createQuery(cq).getResultList();
    }


}
