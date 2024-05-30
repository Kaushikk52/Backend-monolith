package com.ecommerce.demo.services;

import com.ecommerce.demo.models.Product;
import com.ecommerce.demo.repositories.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    public void addProduct(Product product){
        inventoryRepo.save(product);
    }

    public Product getProduct(String productName){
        return inventoryRepo.findByName(productName);
    }

    public Product getProductById(Long productId){
        return inventoryRepo.findById(productId).orElseThrow(()-> new RuntimeException("Product Not found..."));
    }


    public List<Product> getAllProducts(){
        return inventoryRepo.findAll();
    }

    public Product updateProduct(Product product){
        Product existingProduct = inventoryRepo.findByName(product.getName());
        existingProduct.setDesc(product.getDesc());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        return inventoryRepo.save(existingProduct);
    }

    public void removeProduct(String productName){
        Product product = inventoryRepo.findByName(productName);
        inventoryRepo.delete(product);
    }



}
