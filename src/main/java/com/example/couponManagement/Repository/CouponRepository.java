package com.example.couponManagement.Repository;

import com.example.couponManagement.beans.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Query("SELECT entry from Coupon entry WHERE entry.cartWiseCouponDetails.id = ?1")
    Coupon findCartWiseCoupon(Long id);
    @Query("SELECT entry from Coupon entry WHERE entry.productWiseCouponDetails.id = ?1")
    Coupon findProductWiseCoupon(Long id);
    @Query("SELECT entry from Coupon entry WHERE entry.bxgyCouponDetails.id = ?1")
    Coupon findBxgyWiseCoupon(Long id);


}
