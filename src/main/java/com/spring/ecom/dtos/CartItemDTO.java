package com.spring.ecom.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private CartItemProductDTO cartItemProductDTO;
    private int quantity;
    private BigDecimal totalPrice;
}
