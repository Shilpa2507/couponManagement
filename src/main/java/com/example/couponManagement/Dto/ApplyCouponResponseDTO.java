package com.example.couponManagement.Dto;

import lombok.Data;

import java.util.List;

@Data
public class ApplyCouponResponseDTO {
    private List<ItemDTO> items;
    private double totalPrice;
    private double totalDiscount;
    private double finalPrice;
}
