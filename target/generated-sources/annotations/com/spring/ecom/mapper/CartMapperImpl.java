package com.spring.ecom.mapper;

import com.spring.ecom.dtos.CartDTO;
import com.spring.ecom.dtos.CartItemDTO;
import com.spring.ecom.dtos.CartItemProductDTO;
import com.spring.ecom.entities.Cart;
import com.spring.ecom.entities.CartItem;
import com.spring.ecom.entities.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-28T16:18:37+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public CartDTO toDto(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartDTO cartDTO = new CartDTO();

        cartDTO.setId( cart.getId() );

        return cartDTO;
    }

    @Override
    public CartItemDTO toDto(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        CartItemDTO cartItemDTO = new CartItemDTO();

        cartItemDTO.setCartItemProductDTO( toProductDto( cartItem.getProduct() ) );
        if ( cartItem.getQuantity() != null ) {
            cartItemDTO.setQuantity( cartItem.getQuantity() );
        }

        cartItemDTO.setTotalPrice( cartItem.getTotalPrice() );

        return cartItemDTO;
    }

    @Override
    public CartItemProductDTO toProductDto(Product product) {
        if ( product == null ) {
            return null;
        }

        CartItemProductDTO cartItemProductDTO = new CartItemProductDTO();

        cartItemProductDTO.setId( product.getId() );
        cartItemProductDTO.setName( product.getName() );
        cartItemProductDTO.setPrice( product.getPrice() );

        return cartItemProductDTO;
    }
}
