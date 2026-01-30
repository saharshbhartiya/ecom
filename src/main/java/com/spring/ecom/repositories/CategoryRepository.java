package com.spring.ecom.repositories;

import com.spring.ecom.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}
