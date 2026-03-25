package com.spring.ecom.services;

import java.util.Optional;

import com.spring.ecom.dtos.WebhookRequest;
import com.spring.ecom.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
