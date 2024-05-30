package com.ecommerce.demo.controllers;

import com.ecommerce.demo.models.Order;
import com.ecommerce.demo.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders = orderService.getAllOrders();
        if(orders.isEmpty()){
            log.warn("Order Repository is Empty");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(orders);
        }
        log.info("Retrieved all orders :{}",orders);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping(value = "/get/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId){
        try {
            Order order = orderService.getOrderById(orderId);
            log.info("Retrieved order by ID:{}",orderId);
            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (RuntimeException e) {
            log.warn("An Error occurred : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/save/{cartId}")
    public ResponseEntity<?> saveOrder(@PathVariable String cartId){
        try{
            Order order =  orderService.saveOrder(cartId);
            log.info("Order for cart ID {} successfully placed",cartId);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        }catch (Exception e){
            log.error("Error placing order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<String> removeOrderByID(@PathVariable String orderId) {
        try {
            orderService.removeOrderById(orderId);
            log.info("Order deleted: {}", orderId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Order deleted");
        } catch(Exception e) {
            log.warn("An Error occurred : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
