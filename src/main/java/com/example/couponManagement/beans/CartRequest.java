package com.example.couponManagement.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartRequest {
    @JsonProperty("cart")
    private Cart cart;
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
