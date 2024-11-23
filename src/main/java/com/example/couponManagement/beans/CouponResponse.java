package com.example.couponManagement.beans;

import lombok.Data;

@Data
public class CouponResponse {
    private Long couponId;
    private String type;
    private double discount;
}
