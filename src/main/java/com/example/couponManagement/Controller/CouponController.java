package com.example.couponManagement.Controller;

import com.example.couponManagement.Dto.ApplyCouponResponseDTO;
import com.example.couponManagement.Dto.CouponDTO;
import com.example.couponManagement.Exception.CouponExpiredException;
import com.example.couponManagement.Exception.CouponNotApplicableException;
import com.example.couponManagement.Exception.CouponNotFoundException;
import com.example.couponManagement.Service.CouponService;
import com.example.couponManagement.beans.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.DataInput;
import java.util.List;
import java.util.Optional;

@RestController
public class CouponController {
    Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    CouponService couponService;
    @PostMapping("/coupons")
    public ResponseEntity<ApiResponse> createCoupon(@RequestBody Coupon coupon){
        logger.info(coupon.getType());
        logger.info(coupon.getExpirationDate().toString());
        Coupon savedCoupon = couponService.createCoupon(coupon);
        return new ResponseEntity<>(ApiResponse.builder().responseType("success").data(savedCoupon).message("Successfully created a coupon").build(), HttpStatus.CREATED);
    }
    @GetMapping("/coupons")
    public ResponseEntity<ApiResponse> getCoupons(){
        List<CouponDTO> allCoupons = couponService.getCoupons();
        return new ResponseEntity<>(ApiResponse.builder().responseType("success").data(allCoupons).message("Successfully retrieved all the coupons").build(), HttpStatus.OK);
    }
    @GetMapping("/coupons/{id}")
    public ResponseEntity<ApiResponse> getCouponById(@PathVariable("id") Long couponId) {
        try{
            CouponDTO coupon = couponService.getCouponById(couponId);
            return new ResponseEntity<>(ApiResponse.builder().responseType("success").data(coupon).message("Successfully retrieved coupon by id").build(), HttpStatus.OK);
        }
        catch (CouponNotFoundException couponNotFoundException){
            logger.error("Coupon with the provided coupon Id " + couponId + " is not present");
            return new ResponseEntity<>(ApiResponse.builder().responseType("error").message(couponNotFoundException.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }

    }
    @PutMapping("/coupons/{id}")
    public ResponseEntity<ApiResponse> updateCouponById(@PathVariable("id") Long couponId, @RequestBody Coupon coupon){
        try {
            CouponDTO updatedCoupon = couponService.updateCouponById(couponId, coupon);
            return new ResponseEntity<>(ApiResponse.builder().responseType("success").data(updatedCoupon).message("Successfully updated coupon by id").build(), HttpStatus.OK);
        }
        catch(CouponNotFoundException couponNotFoundException){
            logger.error("Coupon with the provided coupon Id " + couponId + " is not present");
            return new ResponseEntity<>(ApiResponse.builder().responseType("error").message(couponNotFoundException.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }


    }
    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<ApiResponse> deleteCouponById(@PathVariable("id") Long couponId){
        try{
            couponService.deleteCouponById(couponId);
            return new ResponseEntity<>(ApiResponse.builder().responseType("success").message("Successfully deleted the coupon").build(), HttpStatus.OK);
        }
        catch (CouponNotFoundException couponNotFoundException){
            logger.error("Coupon with the provided coupon Id " + couponId + " is not present");
            return new ResponseEntity<>(ApiResponse.builder().responseType("error").message(couponNotFoundException.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/applicable-coupons")
    public ResponseEntity<ApiResponse> getApplicableCoupons(@RequestBody CartRequest cartRequest){
       if(!couponService.getApplicableCoupons(cartRequest.getCart()).isEmpty()){
           return new ResponseEntity<>(ApiResponse.builder().responseType("success").data(couponService.getApplicableCoupons(cartRequest.getCart())).message("Successfully fetched the applicable coupons").build(), HttpStatus.OK);
       }
        return new ResponseEntity<>(ApiResponse.builder().responseType("success").data(couponService.getApplicableCoupons(cartRequest.getCart())).message("Sorry, there are no coupons applicable for this cart at this point.").build(), HttpStatus.OK);

    }
    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<ApiResponse> applyCoupon(@PathVariable("id") Long couponId, @RequestBody CartRequest cartRequest) {
        try{
            ApplyCouponResponseDTO response = couponService.applyCouponById(couponId, cartRequest.getCart());
            return new ResponseEntity<>(ApiResponse.builder().responseType("success").data(response).message("Successfully applied the coupon").build(), HttpStatus.OK);
        }
        catch(CouponNotFoundException couponNotFoundException){
            logger.error("Coupon with the provided coupon Id " + couponId + " is not present");
            return new ResponseEntity<>(ApiResponse.builder().responseType("error").message(couponNotFoundException.getMessage()).build(), HttpStatus.NOT_FOUND);
        }
        catch(CouponExpiredException couponExpiredException){
            logger.error("Coupon with the provided coupon Id " + couponId + " has expired.");
            return new ResponseEntity<>(ApiResponse.builder().responseType("error").message(couponExpiredException.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        catch(CouponNotApplicableException couponNotApplicableException){
            logger.error("Sorry, this coupon is not applicable to the cart");
            return new ResponseEntity<>(ApiResponse.builder().responseType("error").message(couponNotApplicableException.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }

    }
}
