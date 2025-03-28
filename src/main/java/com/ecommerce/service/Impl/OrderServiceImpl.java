package com.ecommerce.service.Impl;

import com.ecommerce.Dto.OrderDTO;
import com.ecommerce.Dto.RazorpayVerifyPaymentDTO;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.Orders;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.exception.AddressNotFoundException;
import com.ecommerce.exception.InvalidCreationRazorpayException;
import com.ecommerce.exception.OrderNotFoundException;
import com.ecommerce.exception.product.ProductNotFoundException;
import com.ecommerce.exception.user.UserNotFoundException;
import com.ecommerce.respository.AddressRepository;
import com.ecommerce.respository.OrdersRepository;
import com.ecommerce.respository.ProductRepository;
import com.ecommerce.respository.UserRepository;
import com.ecommerce.service.OrdersService;
import com.ecommerce.service.RazorpayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrdersService {
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RazorpayService razorpayService;


    @Override
    public Map<String, Object> createOrders(Integer user_id, Integer totalPrice) {
        // Validate user existence
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + user_id));
        try{
            return razorpayService.createOrderViaRazorPay(totalPrice,user_id);
        }
        catch (RazorpayException | JsonProcessingException e){
            System.out.println("Razorpay Exception or Json convertion failed "+ e.getMessage());
            throw new InvalidCreationRazorpayException("Payment failed try again later");
        }
    }

    @Override
    public boolean verifyPaymentOfRazorpay(RazorpayVerifyPaymentDTO razorpayVerifyPaymentDTO) {
        if (razorpayService.verifyPayment(
                razorpayVerifyPaymentDTO.getPaymentId(),
                razorpayVerifyPaymentDTO.getOrderId(),
                razorpayVerifyPaymentDTO.getSignature()
        )) {
            // Find the user
            User user = userRepository.findById(razorpayVerifyPaymentDTO.getUserid())
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + razorpayVerifyPaymentDTO.getUserid()));

            // Find the address
            Address address = addressRepository.findById(razorpayVerifyPaymentDTO.getAddressId())
                    .orElseThrow(() -> new AddressNotFoundException("Address not found with ID: " + razorpayVerifyPaymentDTO.getAddressId()));

            // Find all products
            List<Product> products = new ArrayList<>();
            for (Integer pid : razorpayVerifyPaymentDTO.getProductIds()) {
                if (pid == null) {
                    throw new IllegalArgumentException("Product ID in the list must not be null");
                }
                Product product = productRepository.findById(pid)
                        .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + pid));
                products.add(product);
            }

            // Add all products into orders table
            for (Product product : products) {
                Orders order = new Orders();
                order.setUser(user);
                order.setAddress(address);
                order.setProduct(product);
                order.setStatus(OrderStatus.PLACED);
                ordersRepository.save(order);
            }

            return true;
        } else {
            return false;
        }
    }



    @Override
    public List<Product> getOrder(Integer user_id) {
        if (userRepository.existsById(user_id)) {
            return ordersRepository.findOrdersByUserId(user_id)
                    .stream()
                    .map(Orders::getProduct)
                    .collect(Collectors.toList());
        }
        throw new UserNotFoundException("User not found with ID: " + user_id);
    }

    @Override
    public OrderDTO updateStatue(Integer Order_id,OrderStatus status) {
        Orders updatedorder = ordersRepository.findById(Order_id).orElseThrow(
                ()-> new OrderNotFoundException("Order not found with ID: " + Order_id)
        );
        updatedorder.setStatus(status);
        Orders savedOrder = ordersRepository.save(updatedorder);
        return convertToOrderDTO(savedOrder);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Orders> orders = ordersRepository.findAll();

        return orders.stream().map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO convertToOrderDTO(Orders order){
        return new OrderDTO(
                order.getId(),
                order.getStatus(),
                order.getProduct().getName(),
                order.getProduct().getPrice(),
                order.getProduct().getProductUrl()
        );
    }
}



