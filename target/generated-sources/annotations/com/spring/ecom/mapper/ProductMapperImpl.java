package com.spring.ecom.mapper;

import com.spring.ecom.dtos.ProductDTO;
import com.spring.ecom.entities.Category;
import com.spring.ecom.entities.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T16:30:12+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO.ProductDTOBuilder productDTO = ProductDTO.builder();

        productDTO.categoryId( productCategoryId( product ) );
        productDTO.description( product.getDescription() );
        productDTO.id( product.getId() );
        productDTO.name( product.getName() );
        productDTO.price( product.getPrice() );

        return productDTO.build();
    }

    @Override
    public Product toEntity(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.description( productDTO.getDescription() );
        product.id( productDTO.getId() );
        product.name( productDTO.getName() );
        product.price( productDTO.getPrice() );

        return product.build();
    }

    @Override
    public void update(ProductDTO productDTO, Product product) {
        if ( productDTO == null ) {
            return;
        }

        product.setDescription( productDTO.getDescription() );
        product.setName( productDTO.getName() );
        product.setPrice( productDTO.getPrice() );
    }

    private Byte productCategoryId(Product product) {
        Category category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }
}
