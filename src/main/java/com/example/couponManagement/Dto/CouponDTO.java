package com.example.couponManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponDTO {
    private Long id;
    private String type;
    private Timestamp expirationDate;

}
