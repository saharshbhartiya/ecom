package com.spring.ecom.controllers;

import com.spring.ecom.dtos.AddItemToCartRequest;
import com.spring.ecom.dtos.CartDTO;
import com.spring.ecom.dtos.CartItemDTO;
import com.spring.ecom.entities.CartItem;
import com.spring.ecom.mapper.CartMapper;
import com.spring.ecom.repositories.CartRepository;
import com.spring.ecom.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.spring.ecom.entities.Cart;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDTO> createCart(){
        var cart = new Cart();
        cartRepository.save(cart);

        var cartDto = cartMapper.toDto(cart);
        return new ResponseEntity<>(cartDto , HttpStatus.CREATED);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDTO> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request
            ){
        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart==null){
            return ResponseEntity.notFound().build();
        }

        var product = productRepository.findById(request.getProductId()).orElse(null);
        if(product == null){
            return ResponseEntity.badRequest().build();
        }

        var cartItem = cart.getCartItems().stream()
                .filter(item ->{
                    return item.getProduct().getId().equals(product.getId());
                })
                .findFirst()
                .orElse(null);

        if(cartItem != null){
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else{
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }

        cartRepository.save(cart);

        var cartItemDto = cartMapper.toDto(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCart(
            @PathVariable UUID cartId
    ){
        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }
}
