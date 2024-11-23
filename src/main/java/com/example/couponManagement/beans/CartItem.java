package com.example.couponManagement.beans;

import lombok.Data;

@Data
public class CartItem {
    private Integer productId;
    private Integer quantity;
    private double price;
}
