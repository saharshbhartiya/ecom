package com.spring.ecom.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
}
