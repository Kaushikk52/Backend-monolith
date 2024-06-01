package com.ecommerce.demo.services;

import com.ecommerce.demo.models.Cart;
import com.ecommerce.demo.models.Order;
import com.ecommerce.demo.repositories.CartRepo;
import com.ecommerce.demo.repositories.OrderRepo;
import com.ecommerce.demo.repositories.UserRepo;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    PaymentService paymentService;

    public List<Order> getAllOrders(){
        return orderRepo.findAll();
    }

    public Order getOrderById(String orderId){
        return orderRepo.findById(orderId).orElseThrow(()-> new RuntimeException("Order not Found"));
    }

    public Order saveOrder(String cartId) throws RazorpayException {
        Order order = new Order();
        order.setStatus("Pending");
        Cart cart = cartRepo.findById(cartId).orElseThrow(()-> new RuntimeException("Cart not Found"));
        order.setTotal(cart.getPrice());
        order.setProductList(cart.getProducts());

        cart.setProducts(null);
        cart.setQty(0);
        cart.setPrice(0);
        com.razorpay.Order rz_Order=paymentService.createOrder(order.getTotal(), cartId);
        if(rz_Order!=null){
            order.setId(rz_Order.get("id"));
            order.setStatus(rz_Order.get("status"));
            order.setOrderCreation(rz_Order.get("created_at"));
        }
        return orderRepo.save(order);
    }

    public String removeOrderById(String orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(()-> new RuntimeException("Order not found"));
        orderRepo.delete(order);
        return  "Order deleted successfully";
    }

}
