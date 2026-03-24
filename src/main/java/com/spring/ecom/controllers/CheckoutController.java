package com.spring.ecom.controllers;

import com.spring.ecom.dtos.CheckoutRequest;
import com.spring.ecom.dtos.ErrorDTO;
import com.spring.ecom.exceptions.CartEmptyException;
import com.spring.ecom.exceptions.CartNotFoundException;
import com.spring.ecom.services.CheckoutService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequest request
    ) throws StripeException {
        try{
            return ResponseEntity.ok(checkoutService.checkout(request));
        }
        catch (StripeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDTO(resolveStripeMessage(e)));
        }
    }

    @ExceptionHandler({CartNotFoundException.class , CartEmptyException.class})
    public ResponseEntity<ErrorDTO> handleException(Exception ex){
        return ResponseEntity.badRequest().body(new ErrorDTO(ex.getMessage()));
    }

    private String resolveStripeMessage(StripeException exception) {
        var userMessage = exception.getUserMessage();
        if (StringUtils.hasText(userMessage)) {
            return userMessage;
        }

        var message = exception.getMessage();
        if (StringUtils.hasText(message) && message.toLowerCase().contains("invalid api key")) {
            return "Stripe secret key is invalid. Check STRIPE_SECRET_KEY in your .env file.";
        }

        if ("secret_key_required".equals(exception.getCode())) {
            return "Stripe secret key is missing. Check STRIPE_SECRET_KEY in your .env file.";
        }

        return "Error creating checkout session";
    }
}
