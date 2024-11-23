package com.example.couponManagement.Dto;

import lombok.Data;

@Data
public class ItemDTO {
    private Integer productId;
    private Integer quantity;
    private double price;
    private double totalDiscount;
}
