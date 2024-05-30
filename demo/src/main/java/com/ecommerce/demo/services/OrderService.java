package com.ecommerce.demo.services;

import com.ecommerce.demo.models.Cart;
import com.ecommerce.demo.models.Order;
import com.ecommerce.demo.models.User;
import com.ecommerce.demo.repositories.CartRepo;
import com.ecommerce.demo.repositories.OrderRepo;
import com.ecommerce.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    UserRepo userRepo;

    public List<Order> getAllOrders(){
        return orderRepo.findAll();
    }

    public Order getOrderById(String orderId){
        return orderRepo.findById(orderId).orElseThrow(()-> new RuntimeException("Order not Found"));
    }

    public Order saveOrder(String cartId){
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setOrderCreation(new Date());
        order.setStatus("Pending");

        Cart cart = cartRepo.findById(cartId).orElseThrow(()-> new RuntimeException("Cart not Found"));
        order.setTotal(cart.getPrice());
        order.setProductList(cart.getProducts());

        cart.setProducts(null);
        cart.setQty(0);
        cart.setPrice(0);

        return orderRepo.save(order);
    }

    public String removeOrderById(String orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(()-> new RuntimeException("Order not found"));
        orderRepo.delete(order);
        return  "Order deleted successfully";
    }

}
