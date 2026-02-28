package com.spring.ecom.mapper;

import com.spring.ecom.dtos.CartDTO;
import com.spring.ecom.dtos.CartItemDTO;
import com.spring.ecom.dtos.CartItemProductDTO;
import com.spring.ecom.entities.Cart;
import com.spring.ecom.entities.CartItem;
import com.spring.ecom.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDTO toDto(Cart cart);

    @Mapping(target = "totalPrice" , expression = "java(cartItem.getTotalPrice())")
    @Mapping(target = "cartItemProductDTO" , source = "product")
    CartItemDTO toDto(CartItem cartItem);

    @Mapping(target = "id" , source = "product.id")
    @Mapping(target = "name" , source = "product.name")
    @Mapping(target = "price" , source = "product.price")
    CartItemProductDTO toProductDto(Product product);
}
