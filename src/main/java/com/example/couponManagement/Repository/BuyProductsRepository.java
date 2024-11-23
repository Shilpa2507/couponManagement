package com.example.couponManagement.Repository;

import com.example.couponManagement.beans.BuyProducts;
import com.example.couponManagement.beans.GetProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyProductsRepository extends JpaRepository<BuyProducts, Long> {
}
