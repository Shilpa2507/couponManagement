package com.example.couponManagement.Exception;

import lombok.Getter;
import lombok.Setter;


public class CouponNotFoundException extends RuntimeException{
    public CouponNotFoundException() {
    }

    public CouponNotFoundException(String message) {
        super(message);
    }
}
