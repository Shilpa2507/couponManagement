package com.example.couponManagement.Exception;

public class CouponNotApplicableException extends RuntimeException{
    public CouponNotApplicableException() {
    }

    public CouponNotApplicableException(String message) {
        super(message);
    }
}
