package com.example.couponManagement.beans;

import com.example.couponManagement.Service.CouponService;
import com.example.couponManagement.beans.Coupon;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;


public class CouponDeserializer extends JsonDeserializer<Coupon> {
    Logger logger = LoggerFactory.getLogger(CouponService.class);

    @Override
    public Coupon deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);
        String type = node.get("type").asText();
        Coupon couponRequest = new Coupon();
        couponRequest.setType(type);
        String expirationDateString = node.get("expirationDate").asText();
        LocalDateTime localDateTime = LocalDateTime.parse(expirationDateString, DateTimeFormatter.ISO_DATE_TIME);
        Timestamp expirationDate = Timestamp.valueOf(localDateTime);
        couponRequest.setExpirationDate(expirationDate);
        if ("cart-wise".equals(type)) {
            CartWiseCouponDetails details = mapper.treeToValue(node.get("details"), CartWiseCouponDetails.class);
            couponRequest.setCartWiseCouponDetails(details);
        } else if ("product-wise".equals(type)) {
            ProductWiseCouponDetails details = mapper.treeToValue(node.get("details"), ProductWiseCouponDetails.class);
            couponRequest.setProductWiseCouponDetails(details);
            logger.info(couponRequest.getProductWiseCouponDetails().toString());

        } else if ("bxgy".equals(type)) {
            BXGYCouponDetails details = mapper.treeToValue(node.get("details"), BXGYCouponDetails.class);
            couponRequest.setBxgyCouponDetails(details);
        }
        return couponRequest;
    }
}