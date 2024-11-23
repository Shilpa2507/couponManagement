package com.example.couponManagement.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class Cart {
    @JsonProperty("items")
    private List<CartItem> items;
}
