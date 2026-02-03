package com.spring.ecom.repositories;

import com.spring.ecom.dtos.ProductSummaryDTO;
import com.spring.ecom.entities.Category;
import com.spring.ecom.entities.Product;
import com.spring.ecom.repositories.specifications.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>  , ProductCriteriaRepository , JpaSpecificationExecutor<Product> {
    List<Product> findTop5ByNameOrderByPrice(String name);
    List<Product> findFirst5ByNameLikeOrderByPrice(String name);

    List<Product> findByPriceBetweenOrderByName(@Param("min") BigDecimal min , @Param("max") BigDecimal max);

    @Procedure("findProductByPrice")
    List<Product> findProducts(BigDecimal min , BigDecimal max);

    @Modifying
    @Query("update Product p set p.price = :newPrice where p.category.id = :categoryId")
    void updatePriceByCategory(BigDecimal newPrice , Byte categoryId);

    @Query("select new com.spring.ecom.dtos.ProductSummaryDTO(p.id , p.name) from Product p where p.category = :category")
    List<ProductSummaryDTO> findByCategory(@Param("category") Category category);

    @EntityGraph(attributePaths = {"category"})
    Page<Product> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "category")
    List<Product> findByCategoryId(Byte categoryId);

    @EntityGraph(attributePaths = "category")
    @Query("Select p From Product p")
    List<Product> findAllWithCategory();
}
