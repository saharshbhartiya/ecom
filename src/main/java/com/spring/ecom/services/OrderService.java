package com.spring.ecom.services;

import com.spring.ecom.dtos.OrderDTO;
import com.spring.ecom.exceptions.OrderNotFoundException;
import com.spring.ecom.mapper.OrderMapper;
import com.spring.ecom.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDTO> getAllOrder(){
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDTO getOrder(Long orderId) {
        var order =  orderRepository.getOrderWithItems(orderId).orElse(null);
        if(order == null){
            throw new OrderNotFoundException();
        }
        var user = authService.getCurrentUser();
        if(!order.isPlaceBy(user)){
            throw new AccessDeniedException(
                    "You don't have access to this order."
            );
        }
        return orderMapper.toDto(order);
    }
}
