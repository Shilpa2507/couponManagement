package com.example.couponManagement.Repository;

import com.example.couponManagement.beans.ProductWiseCouponDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductWiseCouponRepository extends JpaRepository<ProductWiseCouponDetails, Long> {
}
