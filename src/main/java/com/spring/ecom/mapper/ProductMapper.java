package com.spring.ecom.mapper;

import com.spring.ecom.dtos.CreateProductRequest;
import com.spring.ecom.dtos.ProductDTO;
import com.spring.ecom.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId" , source = "category.id")
    ProductDTO toDto(Product product);

    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "id" , ignore = true)
    void update (ProductDTO productDTO , @MappingTarget Product product);
}
