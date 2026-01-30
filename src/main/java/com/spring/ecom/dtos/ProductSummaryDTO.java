package com.spring.ecom.dtos;

import com.spring.ecom.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductSummaryDTO {
    private Long id;
    private String name;
}
