package com.ecommerce.demo.services;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService {

    @Value("${razorpay.keyId}")
    private String keyId;

    @Value("${razorpay.keySecret}")
    private String keySecret;

    private RazorpayClient client;

    @PostConstruct
    public void init() throws RazorpayException {
        client = new RazorpayClient(keyId, keySecret);
    }

    public Order createOrder(double amount, String id) throws RazorpayException {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount",amount * 100);
            orderRequest.put("currency","INR");
            orderRequest.put("receipt", "txn_"+id);
            //creating order
            Order order = client.Orders.create(orderRequest);
            log.info("RazorPay order created : {}",order);
            return order;

    }
}
