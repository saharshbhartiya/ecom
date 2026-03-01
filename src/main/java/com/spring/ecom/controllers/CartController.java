package com.spring.ecom.controllers;

import com.spring.ecom.dtos.AddItemToCartRequest;
import com.spring.ecom.dtos.CartDTO;
import com.spring.ecom.dtos.CartItemDTO;
import com.spring.ecom.dtos.UpdateCartItemDTO;
import com.spring.ecom.entities.CartItem;
import com.spring.ecom.mapper.CartMapper;
import com.spring.ecom.repositories.CartRepository;
import com.spring.ecom.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.spring.ecom.entities.Cart;

import java.util.Map;
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

        var cartItem = cart.addItem(product);

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

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCart(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @RequestBody UpdateCartItemDTO request
    ){
        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error" , "Cart not Found")
            );
        }

        var cartItem = cart.getItem(productId);

        if(cartItem == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error" , "Product not found in cart")
            );
        }
        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        return ResponseEntity.ok().body(cartMapper.toDto(cartItem));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ){
        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error" , "Cart was not found")
            );
        }
        var product = productRepository.findById(productId).orElse(null);
        if(product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error" , "Product was not found")
            );
        }
        var cartItem = cart.getItem(productId);
        cart.removeItem(productId);
        cartRepository.save(cart);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(
            @PathVariable("cartId") UUID cartId
    ){
        var cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
              Map.of("error" , "Cart Not Found.")
            );
        }

        cart.clear();
        cartRepository.save(cart);
        return ResponseEntity.noContent().build();
    }
}
