package com.ecommerce.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RazorpayVerifyPaymentDTO {
    private Integer userid;
    private Integer addressId;
    private List<Integer> productIds;
    private String paymentId;
    private String orderId;
    private String signature;
}
