package com.spring.ecom.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull(message = "Product id must be provided.")
    private Long productId;
}
