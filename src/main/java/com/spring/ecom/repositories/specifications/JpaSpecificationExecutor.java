package com.spring.ecom.repositories.specifications;

import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface JpaSpecificationExecutor<T> {
    Optional<T> findOne(Specification<T> spec);

    List<T> findAll(@Nullable Specification<T> spec);

    Page<T> findAll(@Nullable Specification<T> spec , Pageable pageable);

    List<T> findAll(@Nullable Specification<T> spec , Sort sort);

    long count(@Nullable Specification<T> spec );

    boolean exists(Specification<T> spec);
}