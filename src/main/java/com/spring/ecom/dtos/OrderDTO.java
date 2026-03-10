package com.spring.ecom.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
    private BigDecimal totalPrice;
}
