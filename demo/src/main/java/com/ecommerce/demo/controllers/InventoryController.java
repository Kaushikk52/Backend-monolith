package com.ecommerce.demo.controllers;

import com.ecommerce.demo.models.Product;
import com.ecommerce.demo.services.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping(value = "/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        try {
            inventoryService.addProduct(product);
            log.info("Product added successfully : {}", product);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (IllegalArgumentException e) {
            log.warn("An Error occurred : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping(value = "/get")
     public ResponseEntity<Product> getProductById(@RequestBody Long productId){
        try {
            Product product = inventoryService.getProductById(productId);
            log.info("Retrieved product by ID:{}",productId);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (RuntimeException e) {
            log.warn("An Error occurred : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/products")
    public ResponseEntity<List<Product>> getProducts(){
        List<Product> products = inventoryService.getAllProducts();
        if (inventoryService.getAllProducts().isEmpty()) {
            log.warn("Product Repository is Empty");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(products);
        }
        log.info("Retrieved all products :{}",products);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<Product> editProduct(Product product){
        try {
            Product updatedProduct = inventoryService.updateProduct(product);
            log.info("Product updated successfully: {}", updatedProduct);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        } catch (IllegalArgumentException e) {
            log.warn("An Error occurred : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
        }
    }

    @DeleteMapping(value = "/remove/{productName}")
    public ResponseEntity<String> removeProduct(@PathVariable String productName){
        try {
            inventoryService.removeProduct(productName);
            log.info("Product deleted successfully: {}", productName);
            return ResponseEntity.status(HttpStatus.OK).body("Product : "+productName+" deleted successfully");
        } catch (IllegalArgumentException e) {
            log.warn("An Error occurred : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



}
