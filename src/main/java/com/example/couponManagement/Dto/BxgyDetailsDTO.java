package com.example.couponManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BxgyDetailsDTO extends CouponDTO{
    private List<BuyProductsDTO> buyProducts;

    private List<GetProductsDTO> getProducts;

    private Integer repetitionLimit;
}
