package com.spring.ecom.mapper;

import com.spring.ecom.dtos.OrderDTO;
import com.spring.ecom.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface OrderMapper {
    OrderDTO toDto(Order order);
}
