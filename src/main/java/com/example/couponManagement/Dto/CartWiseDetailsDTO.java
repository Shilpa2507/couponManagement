package com.example.couponManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartWiseDetailsDTO extends CouponDTO{
    private Long threshold;
    private Long discount;
}
