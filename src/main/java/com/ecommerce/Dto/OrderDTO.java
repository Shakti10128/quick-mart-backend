package com.ecommerce.Dto;

import com.ecommerce.enums.OrderStatus;
import lombok.Data;

import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Integer id;
    private OrderStatus status;
    private String productName;
    private Integer productPrice;
    private String productUrl;
}
