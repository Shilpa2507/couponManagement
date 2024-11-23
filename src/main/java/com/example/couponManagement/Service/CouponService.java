package com.example.couponManagement.Service;
import java.sql.Timestamp;
import java.time.LocalDate;


import com.example.couponManagement.Dto.*;
import com.example.couponManagement.Exception.CouponExpiredException;
import com.example.couponManagement.Exception.CouponNotApplicableException;
import com.example.couponManagement.Exception.CouponNotFoundException;
import com.example.couponManagement.Repository.*;
import com.example.couponManagement.beans.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponService {
    Logger logger = LoggerFactory.getLogger(CouponService.class);

    @Autowired
    CouponRepository couponRepository;
    @Autowired
    BxgyCouponDetailsRepository bxgyCouponDetailsRepository;
    @Autowired
    CartWiseCouponRepository cartWiseCouponRepository;
    @Autowired
    ProductWiseCouponRepository productWiseCouponRepository;
    @Autowired
    GetProductsRepository getProductsRepository;
    @Autowired
    BuyProductsRepository buyProductsRepository;
    public boolean isCouponExpired(Coupon coupon) {
        if (coupon.getExpirationDate() == null) {
            return false;
        }
        return coupon.getExpirationDate().before(new Timestamp(System.currentTimeMillis()));
    }
    @Transactional
    public Coupon createCoupon(Coupon coupon){
        if("cart-wise".equals(coupon.getType())){
            logger.info(String.valueOf(coupon.getExpirationDate()));
            cartWiseCouponRepository.save(coupon.getCartWiseCouponDetails());
        }
        else if("product-wise".equals(coupon.getType())){
            productWiseCouponRepository.save(coupon.getProductWiseCouponDetails());
        }
        else{

            bxgyCouponDetailsRepository.save(coupon.getBxgyCouponDetails());
        }
        return couponRepository.save(coupon);
    }
    public List<CouponDTO> getCoupons(){
        return couponRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private CouponDTO convertToDTO(Coupon coupon){
        if("cart-wise".equals(coupon.getType())){
            CartWiseDetailsDTO dto = new CartWiseDetailsDTO();
            dto.setId(coupon.getId());;
            dto.setType(coupon.getType());
            dto.setExpirationDate(coupon.getExpirationDate());
            dto.setThreshold(coupon.getCartWiseCouponDetails().getThreshold());
            dto.setDiscount(coupon.getCartWiseCouponDetails().getDiscount());
            return dto;
        }
        else if("product-wise".equals(coupon.getType())){
            ProductWiseDetailsDTO dto = new ProductWiseDetailsDTO();
            dto.setId(coupon.getId());;
            dto.setType(coupon.getType());
            dto.setExpirationDate(coupon.getExpirationDate());
            dto.setProductId(coupon.getProductWiseCouponDetails().getProductId());
            dto.setDiscount(coupon.getProductWiseCouponDetails().getDiscount());
            return dto;
        }
        else{
            BxgyDetailsDTO dto = new BxgyDetailsDTO();
            dto.setId(coupon.getId());;
            dto.setType(coupon.getType());
            dto.setExpirationDate(coupon.getExpirationDate());
            dto.setBuyProducts(coupon.getBxgyCouponDetails().getBuyProducts().stream()
                    .map(this::convertToBuyProductDTO).collect(Collectors.toList()));
            dto.setGetProducts(coupon.getBxgyCouponDetails().getGetProducts().stream()
                    .map(this::convertToGetProductDTO).collect(Collectors.toList()));
            dto.setRepetitionLimit(coupon.getBxgyCouponDetails().getRepetitionLimit());
            return dto;

        }

    }
    private BuyProductsDTO convertToBuyProductDTO(BuyProducts productQuantity) {
        BuyProductsDTO dto = new BuyProductsDTO();
        dto.setProductId(productQuantity.getProductId());
        dto.setQuantity(productQuantity.getQuantity());
        return dto;
    }
    private GetProductsDTO convertToGetProductDTO(GetProducts productQuantity) {
        GetProductsDTO dto = new GetProductsDTO();
        dto.setProductId(productQuantity.getProductId());
        dto.setQuantity(productQuantity.getQuantity());
        return dto;
    }
    public CouponDTO getCouponById(Long couponId){
        Optional<Coupon> coupon = couponRepository.findById(couponId);
        if (coupon.isPresent()) {
            return convertToDTO(coupon.get());
        } else {
            logger.error("Coupon with the provided coupon Id " + couponId + " is not present");
            throw new CouponNotFoundException("Coupon with the provided coupon Id " + couponId + " is not present");
        }
    }

    @Transactional
    public void deleteCouponById(Long couponId){
        Optional<Coupon> coupon = couponRepository.findById(couponId);
        if(coupon.isPresent()){
            Coupon existingCoupon = coupon.get();
            if("cart-wise".equals(existingCoupon.getType())) {
                CartWiseCouponDetails cartWiseCouponDetails = existingCoupon.getCartWiseCouponDetails();
                cartWiseCouponRepository.deleteById(cartWiseCouponDetails.getId());
            }
            else if("product-wise".equals(existingCoupon.getType())) {
                ProductWiseCouponDetails productWiseCouponDetails = existingCoupon.getProductWiseCouponDetails();
                productWiseCouponRepository.deleteById(productWiseCouponDetails.getId());
            }
            else{
                BXGYCouponDetails bxgyCouponDetails = existingCoupon.getBxgyCouponDetails();
                bxgyCouponDetailsRepository.deleteById(bxgyCouponDetails.getId());
            }
            couponRepository.deleteById(couponId);
        }
        else{
            logger.error("Coupon with the provided coupon Id " + couponId + " is not present");
            throw new CouponNotFoundException("Coupon with the provided coupon Id " + couponId + " is not present");
        }

    }

    @Transactional
    public CouponDTO updateCouponById(Long id, Coupon couponRequest) {
        Optional<Coupon> existingCouponOpt = couponRepository.findById(id);
        if (existingCouponOpt.isPresent()) {
            Coupon existingCoupon = existingCouponOpt.get();
            existingCoupon.setType(couponRequest.getType());
            existingCoupon.setExpirationDate(couponRequest.getExpirationDate());
            if ("cart-wise".equals(couponRequest.getType())) {
                CartWiseCouponDetails newCartWiseDetails = couponRequest.getCartWiseCouponDetails();
                CartWiseCouponDetails existingCartWiseDetails = existingCoupon.getCartWiseCouponDetails();
                if (newCartWiseDetails != null) {
                    existingCartWiseDetails.setThreshold(newCartWiseDetails.getThreshold());
                    existingCartWiseDetails.setDiscount(newCartWiseDetails.getDiscount());
                    cartWiseCouponRepository.save(existingCartWiseDetails);
                }
            } else if ("product-wise".equals(couponRequest.getType())) {
                ProductWiseCouponDetails newCouponWiseDetails = couponRequest.getProductWiseCouponDetails();
                ProductWiseCouponDetails existingCouponWiseCouponDetails = existingCoupon.getProductWiseCouponDetails();
                if (newCouponWiseDetails != null) {
                    existingCouponWiseCouponDetails.setProductId(newCouponWiseDetails.getProductId());
                    existingCouponWiseCouponDetails.setDiscount(newCouponWiseDetails.getDiscount());
                    productWiseCouponRepository.save(existingCouponWiseCouponDetails);
                }
            } else if ("bxgy".equals(couponRequest.getType())) {
                BXGYCouponDetails newBxgyDetails = couponRequest.getBxgyCouponDetails();
                BXGYCouponDetails existingBxgyDetails = existingCoupon.getBxgyCouponDetails();
                if (newBxgyDetails != null) {
                    existingBxgyDetails.setBuyProducts(newBxgyDetails.getBuyProducts().stream()
                            .map(this::convertToBuyProduct).collect(Collectors.toList()));
                    existingBxgyDetails.setGetProducts(newBxgyDetails.getGetProducts().stream()
                            .map(this::convertToGetProduct).collect(Collectors.toList()));
                    existingBxgyDetails.setRepetitionLimit(newBxgyDetails.getRepetitionLimit());
                    bxgyCouponDetailsRepository.save(existingBxgyDetails);
                    logger.info("updated coupon");
                }
            }
            else{
                logger.info("no instance found for  coupon");
            }
            Coupon updatedCoupon = couponRepository.save(existingCoupon);
            return convertToDTO(updatedCoupon);
        }
        else {
            logger.error("Coupon with the provided coupon Id " + id + " is not present");
            throw new CouponNotFoundException("Coupon with the provided coupon Id " + id + " is not present");
        }
    }
    private BuyProducts convertToBuyProduct(BuyProducts product){
        BuyProducts productQuantity = new BuyProducts();
        productQuantity.setProductId(product.getProductId());
        productQuantity.setQuantity(product.getQuantity());
        return productQuantity;
    }
    private GetProducts convertToGetProduct(GetProducts product){
        GetProducts productQuantity = new GetProducts();
        productQuantity.setProductId(product.getProductId());
        productQuantity.setQuantity(product.getQuantity());
        return productQuantity;
    }
    public List<CouponResponse> getApplicableCoupons(Cart cart){
       List<CouponResponse> applicableCoupons = new ArrayList<>();
       logger.info(cart.getItems().toString());
       applicableCoupons.addAll(getApplicableCartWiseCoupon(cart));
       applicableCoupons.addAll(getApplicableProductWiseCoupon(cart));
       applicableCoupons.addAll(getApplicableBxgyCoupons(cart));
       return applicableCoupons;
    }
    private List<CouponResponse> getApplicableCartWiseCoupon(Cart cart){
        List<CouponResponse> applicableCoupons = new ArrayList<>();
        double totalCartValue = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
        logger.info(String.valueOf(totalCartValue));
        List<CartWiseCouponDetails> availableCartWiseCoupons = cartWiseCouponRepository.findAll();
        for(CartWiseCouponDetails cartCoupons : availableCartWiseCoupons){
            Coupon coupon = couponRepository.findCartWiseCoupon(cartCoupons.getId());
            if(!isCouponExpired(coupon)){
                if(totalCartValue >= cartCoupons.getThreshold()){
                    CouponResponse response = new CouponResponse();
                    response.setCouponId(coupon.getId());
                    response.setType(coupon.getType());
                    response.setDiscount(totalCartValue*((double) cartCoupons.getDiscount() / 100));
                    applicableCoupons.add(response);

                }
            }
        }
        return applicableCoupons;

    }

    private List<CouponResponse> getApplicableProductWiseCoupon(Cart cart){
        List<CouponResponse> applicableCoupons = new ArrayList<>();
        List<ProductWiseCouponDetails> availableProductWiseCoupons = productWiseCouponRepository.findAll();

        for (ProductWiseCouponDetails coupon : availableProductWiseCoupons) {
            Coupon couponDetails = couponRepository.findProductWiseCoupon(coupon.getId());
            if(!isCouponExpired(couponDetails)){
                Optional<CartItem> cartItem = cart.getItems().stream()
                        .filter(item -> Objects.equals(item.getProductId(), coupon.getProductId()))
                        .findFirst();
                if (cartItem.isPresent()) {
                    double discount = cartItem.get().getQuantity() * cartItem.get().getPrice() * ((double) coupon.getDiscount() / 100);
                    CouponResponse response = new CouponResponse();
                    response.setCouponId(couponDetails.getId());
                    response.setType("product-wise");
                    response.setDiscount(discount);
                    applicableCoupons.add(response);
                }

            }
        }
        return applicableCoupons;
    }
    private List<CouponResponse> getApplicableBxgyCoupons(Cart cart) {
        List<CouponResponse> applicableCoupons = new ArrayList<>();
        List<BXGYCouponDetails> bxgyCoupons = bxgyCouponDetailsRepository.findAll();
        for (BXGYCouponDetails coupon : bxgyCoupons) {
            Coupon couponDetails = couponRepository.findBxgyWiseCoupon(coupon.getId());
            if(!isCouponExpired(couponDetails)){
                int totalBuyCount = 0;
                for(BuyProducts buy : coupon.getBuyProducts()) {
                    Optional<CartItem> cartItem = cart.getItems().stream()
                            .filter(item -> item.getProductId() == buy.getProductId())
                            .findFirst();
                    if (cartItem.isPresent()) {
                        totalBuyCount += cartItem.get().getQuantity();
                    }
                }
                int maxApplications = totalBuyCount / coupon.getBuyProducts().get(0).getQuantity();
                maxApplications = Math.min(maxApplications, coupon.getRepetitionLimit());
                if(maxApplications > 0){
                    for(GetProducts getProduct : coupon.getGetProducts()) {
                        Optional<CartItem> freeItem = cart.getItems().stream()
                                .filter(item -> item.getProductId() == getProduct.getProductId())
                                .findFirst();
                        if(freeItem.isPresent()) {
                            double discount = maxApplications * getProduct.getQuantity() * freeItem.get().getPrice();
                            CouponResponse response = new CouponResponse();
                            response.setCouponId(couponDetails.getId());
                            response.setType("bxgy");
                            response.setDiscount(discount);
                            applicableCoupons.add(response);
                        }
                    }
                }

            }
        }
        return applicableCoupons;
    }
    public ApplyCouponResponseDTO applyCouponById(Long couponId, Cart cart){
        Optional<Coupon> coupon = couponRepository.findById(couponId);
        if(coupon.isPresent()){
            Coupon selectedCoupon = coupon.get();
            if (!isCouponApplicable(couponId, cart)) {
                logger.error("Coupon with the provided coupon Id " + couponId + " is not applicable for this cart");
                throw new CouponNotApplicableException("The coupon is not applicable for this cart.");

            }
            if(isCouponExpired(selectedCoupon)){
                logger.error("Coupon with the provided coupon Id " + couponId + " has expired");
                throw new CouponExpiredException("Coupon with the provided coupon Id " + couponId + " has expired");
            }
            if(selectedCoupon.getType().equals("cart-wise")){
              return ApplyCartWiseCoupon(selectedCoupon, cart);
            }
            else if(selectedCoupon.getType().equals("product-wise")){
              return  ApplyProductWiseCoupon(selectedCoupon, cart);
            }
            else if(selectedCoupon.getType().equals("bxgy")){
                return ApplyBxgyCoupon(selectedCoupon, cart);
            }
        }
        else{
            logger.error("Coupon with the provided coupon Id " + couponId + " is not present");
            throw new CouponNotFoundException("Coupon with the provided coupon Id " + couponId + " is not present");
        }
        return new ApplyCouponResponseDTO();
    }

    private  ApplyCouponResponseDTO ApplyCartWiseCoupon(Coupon coupon, Cart cart) {
        List<ItemDTO> items = new ArrayList<>();
        double totalDiscount = 0;
        double totalPrice = 0;

        cart.getItems().forEach(item -> {
            double discountValue = item.getQuantity() * item.getPrice() *
                    ((double) coupon.getCartWiseCouponDetails().getDiscount() / 100);
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setProductId(item.getProductId());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setTotalDiscount(discountValue);
            items.add(itemDTO);

        });
        for (ItemDTO itemDTO : items) {
            totalDiscount += itemDTO.getTotalDiscount();
            totalPrice += itemDTO.getQuantity() * itemDTO.getPrice();
        }
        ApplyCouponResponseDTO response = new ApplyCouponResponseDTO();
        response.setItems(items);
        response.setTotalDiscount(totalDiscount);
        response.setTotalPrice(totalPrice);
        response.setFinalPrice(totalPrice - totalDiscount);
        return response;
    }
    private ApplyCouponResponseDTO ApplyProductWiseCoupon(Coupon coupon, Cart cart) {
        List<ItemDTO> items = new ArrayList<>();
        double totalDiscount = 0;
        double totalPrice = 0;

        cart.getItems().forEach(item -> {
            double discountedValue = 0;
            if(item.getProductId().equals(coupon.getProductWiseCouponDetails().getProductId())){
                discountedValue = item.getQuantity() * item.getPrice() * ((double) coupon.getProductWiseCouponDetails().getDiscount() / 100);
            }
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setProductId(item.getProductId());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setTotalDiscount(discountedValue);
            items.add(itemDTO);

        });
        for (ItemDTO itemDTO : items) {
            totalDiscount += itemDTO.getTotalDiscount();
            totalPrice += itemDTO.getQuantity() * itemDTO.getPrice();
        }
        ApplyCouponResponseDTO response = new ApplyCouponResponseDTO();
        response.setItems(items);
        response.setTotalDiscount(totalDiscount);
        response.setTotalPrice(totalPrice);
        response.setFinalPrice(totalPrice - totalDiscount);
        return response;

    }
    public ApplyCouponResponseDTO ApplyBxgyCoupon( Coupon coupon, Cart cart) {
        ApplyCouponResponseDTO response = new ApplyCouponResponseDTO();
        List<ItemDTO> items = new ArrayList<>();
        double totalPrice = 0;
        double totalDiscount = 0;
        int totalBuyCount = 0;
        for (BuyProducts buy : coupon.getBxgyCouponDetails().getBuyProducts()) {
            Optional<CartItem> cartItem = cart.getItems().stream()
                    .filter(item -> item.getProductId() == buy.getProductId())
                    .findFirst();
            if (cartItem.isPresent()) {
                totalBuyCount += cartItem.get().getQuantity();
            }
        }
        int buyQuantity = coupon.getBxgyCouponDetails().getBuyProducts().get(0).getQuantity();
        int maxApplications = totalBuyCount / buyQuantity;
        maxApplications = Math.min(maxApplications, coupon.getBxgyCouponDetails().getRepetitionLimit());
        if (maxApplications > 0) {
            for (CartItem cartItem : cart.getItems()) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setProductId(cartItem.getProductId());
                itemDTO.setQuantity(cartItem.getQuantity());
                itemDTO.setPrice(cartItem.getPrice());
                Optional<GetProducts> getProduct = coupon.getBxgyCouponDetails().getGetProducts().stream()
                        .filter(product -> product.getProductId() == cartItem.getProductId())
                        .findFirst();

                if (getProduct.isPresent()) {
                    int freeQuantity = maxApplications * getProduct.get().getQuantity();
                    double discount = freeQuantity * cartItem.getPrice();
                    itemDTO.setQuantity(cartItem.getQuantity() + freeQuantity); // Update quantity
                    itemDTO.setTotalDiscount(discount);
                    totalDiscount += discount;
                    totalPrice += (cartItem.getQuantity() + freeQuantity) * cartItem.getPrice();

                } else {
                    itemDTO.setTotalDiscount(0);
                    totalPrice += cartItem.getQuantity() * cartItem.getPrice();
                }

                items.add(itemDTO);
            }
        } else {
            for (CartItem cartItem : cart.getItems()) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setProductId(cartItem.getProductId());
                itemDTO.setQuantity(cartItem.getQuantity());
                itemDTO.setPrice(cartItem.getPrice());
                itemDTO.setTotalDiscount(0);
                items.add(itemDTO);
                totalPrice += cartItem.getQuantity() * cartItem.getPrice();
            }
        }
        response.setItems(items);
        response.setTotalPrice(totalPrice);
        response.setTotalDiscount(totalDiscount);
        response.setFinalPrice(totalPrice - totalDiscount);

        return response;
    }
    public boolean isCouponApplicable(Long couponId, Cart cart) {
        List<CouponResponse> applicableCoupons = getApplicableCoupons(cart);

        return applicableCoupons.stream()
                .anyMatch(coupon -> Objects.equals(coupon.getCouponId(), couponId));
    }

}
