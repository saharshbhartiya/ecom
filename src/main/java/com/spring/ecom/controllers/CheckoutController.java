package com.spring.ecom.controllers;

import com.spring.ecom.dtos.CheckoutRequest;
import com.spring.ecom.dtos.CheckoutResponse;
import com.spring.ecom.dtos.ErrorDTO;
import com.spring.ecom.exceptions.CartEmptyException;
import com.spring.ecom.exceptions.CartNotFoundException;
import com.spring.ecom.services.CheckoutService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout(
            @Valid @RequestBody CheckoutRequest request
    ) throws StripeException {
        return checkoutService.checkout(request);
    }

    @ExceptionHandler({CartNotFoundException.class , CartEmptyException.class})
    public ResponseEntity<ErrorDTO> handleException(Exception ex){
        return ResponseEntity.badRequest().body(new ErrorDTO(ex.getMessage()));
    }
}
