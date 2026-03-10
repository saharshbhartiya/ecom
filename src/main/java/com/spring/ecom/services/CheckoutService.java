package com.spring.ecom.services;

import com.spring.ecom.dtos.CheckoutRequest;
import com.spring.ecom.dtos.CheckoutResponse;
import com.spring.ecom.dtos.ErrorDTO;
import com.spring.ecom.entities.Order;
import com.spring.ecom.exceptions.CartEmptyException;
import com.spring.ecom.exceptions.CartNotFoundException;
import com.spring.ecom.repositories.CartRepository;
import com.spring.ecom.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckoutService {

    private final AuthService authService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Value("${websiteUrl}")
    private String websiteUrl;

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
        var builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl( websiteUrl + "/checkout-success?orderId=" + order.getId())
                .setCancelUrl( websiteUrl + "/checkout-cancel");

        order.getItems().forEach(item ->{
            var lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(Long.valueOf(item.getQuantity()))
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setUnitAmountDecimal(item.getUnitPrice())
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

        var session = Session.create(builder.build());

        session.getUrl();

        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId() , session.getUrl());
    }
}
