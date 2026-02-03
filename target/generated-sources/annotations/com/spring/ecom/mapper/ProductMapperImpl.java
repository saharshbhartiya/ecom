package com.spring.ecom.mapper;

import com.spring.ecom.dtos.ProductDTO;
import com.spring.ecom.entities.Category;
import com.spring.ecom.entities.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-03T14:06:57+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
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
        productDTO.id( product.getId() );
        productDTO.name( product.getName() );
        productDTO.description( product.getDescription() );
        productDTO.price( product.getPrice() );

        return productDTO.build();
    }

    @Override
    public Product toEntity(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( productDTO.getId() );
        product.name( productDTO.getName() );
        product.description( productDTO.getDescription() );
        product.price( productDTO.getPrice() );

        return product.build();
    }

    @Override
    public void update(ProductDTO productDTO, Product product) {
        if ( productDTO == null ) {
            return;
        }

        product.setName( productDTO.getName() );
        product.setDescription( productDTO.getDescription() );
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
