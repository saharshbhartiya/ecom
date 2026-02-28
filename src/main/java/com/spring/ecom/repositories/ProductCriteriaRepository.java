package com.spring.ecom.repositories;

import com.spring.ecom.entities.Product;
import java.math.BigDecimal;
import java.util.List;

public interface ProductCriteriaRepository {
    List<Product> findProductsByCriteria(String name , BigDecimal minPrice , BigDecimal maxPrice);
}
