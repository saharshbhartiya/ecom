package com.spring.ecom.exceptions;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(){
        super("Cart is Empty");
    }
}
