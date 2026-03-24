package com.spring.ecom.services;

import com.spring.ecom.dtos.CheckoutRequest;
import com.spring.ecom.dtos.CheckoutResponse;
import com.spring.ecom.entities.Order;
import com.spring.ecom.exceptions.CartEmptyException;
import com.spring.ecom.exceptions.CartNotFoundException;
import com.spring.ecom.repositories.CartRepository;
import com.spring.ecom.repositories.OrderRepository;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    private final AuthService authService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final StripeClient stripeClient;

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) throws StripeException {
        var cart = cartRepository.findById(request.getCartId()).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        if(cart.isEmpty()){
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart , authService.getCurrentUser());

        orderRepository.save(order);

        //Checkout Session
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel");

            order.getItems().forEach(item -> {
                var lineItem = SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(item.getQuantity()))
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("aud")
                                        .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getProduct().getName())
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();
                builder.addLineItem(lineItem);
            });

            var session = stripeClient.checkout().sessions().create(builder.build());

            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getUrl());
        }
        catch (StripeException e){
            logger.error("Stripe checkout session creation failed: {}", e.getMessage());
            orderRepository.delete(order);
            throw e;
        }
    }
}
