package com.example.couponManagement.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class BXGYCouponDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bxgy_details_id")
    private List<GetProducts> getProducts;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bxgy_details_id")
    private List<BuyProducts> buyProducts;

    private Integer repetitionLimit;
}
