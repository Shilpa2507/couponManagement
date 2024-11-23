package com.example.couponManagement.Repository;

import com.example.couponManagement.beans.CartWiseCouponDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartWiseCouponRepository extends JpaRepository<CartWiseCouponDetails, Long> {
}
