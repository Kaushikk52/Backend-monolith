package com.ecommerce.demo.repositories;

import com.ecommerce.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends JpaRepository<Product,Long> {

    Product findByName(String productName);
}
