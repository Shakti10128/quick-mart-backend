package com.ecommerce.controller;

import com.ecommerce.Dto.OrderDTO;
import com.ecommerce.Dto.RazorpayVerifyPaymentDTO;
import com.ecommerce.entity.Orders;
import com.ecommerce.entity.Product;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrdersService ordersService;


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/create-orders/{userid}/{totalPrice}")
    public ResponseEntity<Map<String,Object>> createOrders(
            @PathVariable("userid") Integer userid,
            @PathVariable("totalPrice") Integer totalPrice) {

        Map<String,Object> response = ordersService.createOrders(userid,totalPrice);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/verify-payment")
    public ResponseEntity<Map<String,Object>> verifyPayment(@RequestBody RazorpayVerifyPaymentDTO  razorpayVerifyPaymentDTO) {
        boolean isVerifyPayment = ordersService.verifyPaymentOfRazorpay(razorpayVerifyPaymentDTO);

        Map<String,Object> response = Map.of(
                "success",isVerifyPayment,
                "message",isVerifyPayment ? "Payment success" : "Payment failed"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user-orders/{userid}")
    public ResponseEntity<Map<String,Object>> getUserOrders(@PathVariable("userid") Integer user_id){
        List<Product> userOrders = ordersService.getOrder(user_id);
        Map<String,Object> apiResponse = Map.of(
                "success",true,
                "message","Order Fetched Successfully",
                "data",userOrders
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-order/{orderid}/{status}")
    public ResponseEntity<Map<String,Object>> updateOrderState(@PathVariable("orderid") Integer orderid
    ,@PathVariable("status") OrderStatus status){

        OrderDTO order = ordersService.updateStatue(orderid,status);
        Map<String,Object> response = Map.of(
                "success",true,
                "message","Order Status Updated",
                "data",order
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/allOrders")
    public ResponseEntity<Map<String,Object>> getAllOrders(){
        List<OrderDTO> orders = ordersService.getAllOrders();
        Map<String,Object> response = Map.of(
                "success",true,
                "message","fetch all orders successfully",
                "data", orders
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
