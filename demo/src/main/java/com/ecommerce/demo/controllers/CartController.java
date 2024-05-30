package com.ecommerce.demo.controllers;

import com.ecommerce.demo.models.Cart;
import com.ecommerce.demo.models.Product;
import com.ecommerce.demo.repositories.InventoryRepo;
import com.ecommerce.demo.services.CartService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    InventoryRepo inventoryRepo;

    @PostMapping("/addToCart/{cartId}")
    public ResponseEntity<?> addToCart(@PathVariable String cartId, @RequestParam Long productId){
        try {
            Cart cart = cartService.addToCart(cartId,productId);
            log.info("{} Product {} added to cart {} ",cart.getQty(),productId,cartId);
            return ResponseEntity.status(HttpStatus.CREATED).body(cart);
        } catch (IllegalArgumentException e) {
            log.warn("An Error occurred : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An Error occurred : " +e.getMessage());
        } catch(Exception e) {
            log.warn("An Error occurred : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("An Error occurred : " +e.getMessage());
        }
    }

    @PostMapping(value = "/removeFromCart/{cartId}")
    public ResponseEntity<?> removeFromCart(@PathVariable String cartId, @RequestParam String productName){
        try {
            Product product = inventoryRepo.findByName(productName);
            Cart cart = cartService.removeFromCart(cartId,product);
            log.info("{} Product {} removed from cart {} ",cart.getQty(),product.getId(),cartId);
            return ResponseEntity.status(HttpStatus.OK).body(cart);
        } catch (Exception e) {
            log.warn("An Error occurred : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteCart/{cartId}")
    public ResponseEntity<?> deleteCart(@PathVariable String cartId){
        try{
            cartService.removeCart(cartId);
            log.info("Cart {} deleted successfully ", cartId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Cart deleted successfully");
        }catch(Exception e){
            log.warn("An Error occurred : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
