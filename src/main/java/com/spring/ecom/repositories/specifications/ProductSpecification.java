package com.spring.ecom.repositories.specifications;

import com.spring.ecom.entities.Product;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> fetchCategory(){
        return ((root, query, criteriaBuilder) -> {
            if ((Product.class.equals(query.getResultType()))){
                root.fetch("category" , JoinType.LEFT);
                query.distinct(true);
            }
            return criteriaBuilder.conjunction();
        });
    }
    public static Specification<Product> hasName(String name){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name") , "%" + name + "%"));
    }

    public static Specification<Product> hasPriceGreaterThanEqualTo(BigDecimal price){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price") , price));
    }

    public static Specification<Product> hasPriceLessThanEqualTo(BigDecimal price){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price") , price));
    }
}
