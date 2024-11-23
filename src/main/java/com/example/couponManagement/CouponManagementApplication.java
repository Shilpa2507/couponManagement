package com.example.couponManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.couponManagement.beans")

public class CouponManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponManagementApplication.class, args);
	}

}
