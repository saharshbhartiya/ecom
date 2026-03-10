package com.spring.ecom.controllers;

import com.spring.ecom.dtos.ErrorDTO;
import com.spring.ecom.dtos.OrderDTO;
import com.spring.ecom.exceptions.OrderNotFoundException;
import com.spring.ecom.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDTO> getAllOrder(){
        return orderService.getAllOrder();
    }

    @GetMapping("/{orderId}")
    public OrderDTO getOrder(@PathVariable("orderId") Long orderId){
        return orderService.getOrder(orderId);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Void> handleOrderNotFound(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAccessDenied(Exception ex){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorDTO(ex.getMessage()));
    }
}
