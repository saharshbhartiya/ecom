package com.spring.ecom.mapper;

import com.spring.ecom.dtos.CartDTO;
import com.spring.ecom.dtos.CartItemDTO;
import com.spring.ecom.dtos.CartItemProductDTO;
import com.spring.ecom.entities.Cart;
import com.spring.ecom.entities.CartItem;
import com.spring.ecom.entities.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-10T14:36:06+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public CartDTO toDto(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartDTO cartDTO = new CartDTO();

        cartDTO.setItems( cartItemSetToCartItemDTOList( cart.getCartItems() ) );
        cartDTO.setId( cart.getId() );

        cartDTO.setTotalPrice( cart.getTotalPrice() );

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

    protected List<CartItemDTO> cartItemSetToCartItemDTOList(Set<CartItem> set) {
        if ( set == null ) {
            return null;
        }

        List<CartItemDTO> list = new ArrayList<CartItemDTO>( set.size() );
        for ( CartItem cartItem : set ) {
            list.add( toDto( cartItem ) );
        }

        return list;
    }
}
