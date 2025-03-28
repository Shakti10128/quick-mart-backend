package com.ecommerce.service;

import com.ecommerce.Dto.OrderDTO;
import com.ecommerce.Dto.RazorpayVerifyPaymentDTO;
import com.ecommerce.entity.Orders;
import com.ecommerce.entity.Product;
import com.ecommerce.enums.OrderStatus;

import java.util.List;
import java.util.Map;

public interface OrdersService {
    Map<String, Object> createOrders(Integer user_id, Integer totalPrice);

    boolean  verifyPaymentOfRazorpay(RazorpayVerifyPaymentDTO razorpayVerifyPaymentDTO);

    List<Product> getOrder(Integer user_id);

    OrderDTO updateStatue(Integer Order_id, OrderStatus orderStatus);

    List<OrderDTO> getAllOrders();
}
