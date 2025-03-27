package com.ecommerce.Dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private Integer user_id;
    private Integer totalPrice;
    private Integer addressId;
    private List<Integer> productIds;
}
