package com.spring.ecom.controllers;

import com.spring.ecom.dtos.CheckoutRequest;
import com.spring.ecom.dtos.CheckoutResponse;
import com.spring.ecom.dtos.ErrorDTO;
import com.spring.ecom.exceptions.CartEmptyException;
import com.spring.ecom.exceptions.CartNotFoundException;
import com.spring.ecom.exceptions.PaymentException;
import com.spring.ecom.services.CheckoutService;
import com.spring.ecom.dtos.WebhookRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {



    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout(
            @Valid @RequestBody CheckoutRequest request
    ) {
            return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
    public void handleWebHook(
        @RequestHeader Map<String , String> headers,
        @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers , payload));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDTO("Error creatign a checkout session"));
    }

    @ExceptionHandler({CartNotFoundException.class , CartEmptyException.class})
    public ResponseEntity<ErrorDTO> handleException(Exception ex){
        return ResponseEntity.badRequest().body(new ErrorDTO(ex.getMessage()));
    }
}
