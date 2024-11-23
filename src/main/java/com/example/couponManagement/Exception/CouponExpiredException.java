package com.example.couponManagement.Exception;

public class CouponExpiredException extends RuntimeException{
    public CouponExpiredException() {
    }

    public CouponExpiredException(String message) {
        super(message);
    }
}
