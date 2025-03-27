package com.ecommerce.service;

import com.ecommerce.exception.InvalidCreationRazorpayException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.razorpay.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RazorpayService {
    @Value("${RAZORPAY_API_KEY}")
    public String razorpay_api_key;

    @Value("${RAZORPAY_API_SECRET}")
    public String razorpay_api_secret;

    public Map<String, Object> createOrderViaRazorPay(Integer totalAmount,Integer userId) throws RazorpayException, JsonProcessingException {
        RazorpayClient razorpayClient = new RazorpayClient(razorpay_api_key,razorpay_api_secret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", totalAmount * 100); // Razorpay expects the amount in paise (1 INR = 100 paise)
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "receipt_" + userId); // Unique identifier for the transaction
        orderRequest.put("payment_capture", 1); // Auto-capture payment

        Order order = razorpayClient.orders.create(orderRequest);

        // Initialize response
        Map<String, Object> response = new HashMap<>();

        try {
            // ✅ FIX: Handle JSON Parsing properly
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> razorpayResponse = objectMapper.readValue(order.toString(), new TypeReference<>() {});

            response.put("razorpayResponse", razorpayResponse);
            response.put("success", true);
        } catch (JsonProcessingException e) {
            response.put("message", "Error processing Razorpay response");
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    public boolean verifyPayment(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature) {
        try {
//            System.out.println(razorpayPaymentId);
//            System.out.println(razorpayOrderId);
//            System.out.println(razorpaySignature);
            RazorpayClient razorpayClient = new RazorpayClient(razorpay_api_key, razorpay_api_secret);

            JSONObject paymentDetails = new JSONObject();
            paymentDetails.put("razorpay_payment_id", razorpayPaymentId);
            paymentDetails.put("razorpay_order_id", razorpayOrderId);
            paymentDetails.put("razorpay_signature", razorpaySignature);

            boolean isPaymentValid = Utils.verifyPaymentSignature(paymentDetails, razorpay_api_secret);
            if (isPaymentValid) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new InvalidCreationRazorpayException("Invalid Razorpay Payment");
        }
    }


}
