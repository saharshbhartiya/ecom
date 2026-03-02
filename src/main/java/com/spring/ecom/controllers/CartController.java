package com.spring.ecom.controllers;

import com.spring.ecom.dtos.AddItemToCartRequest;
import com.spring.ecom.dtos.CartDTO;
import com.spring.ecom.dtos.CartItemDTO;
import com.spring.ecom.dtos.UpdateCartItemDTO;
import com.spring.ecom.exceptions.CartNotFoundException;
import com.spring.ecom.exceptions.ProductNotFoundException;
import com.spring.ecom.mapper.CartMapper;
import com.spring.ecom.repositories.CartRepository;
import com.spring.ecom.repositories.ProductRepository;
import com.spring.ecom.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDTO> createCart(
            UriComponentsBuilder uriBuilder
    ){
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public CartItemDTO addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request
            ){
        return cartService.addToCart(cartId , request.getProductId());
    }

    @GetMapping("/{cartId}")
    public CartDTO getCart(
            @PathVariable UUID cartId
    ){
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDTO updateCart(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @RequestBody UpdateCartItemDTO request
    ){
        return cartService.updateCart(cartId , productId , request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ){
        cartService.removeItem(cartId , productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(
            @PathVariable("cartId") UUID cartId
    ){
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String , String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error" , "Cart not found."));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String , String>> handleProductNotFount(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error" , "Product not found in the cart."));
    }
}
