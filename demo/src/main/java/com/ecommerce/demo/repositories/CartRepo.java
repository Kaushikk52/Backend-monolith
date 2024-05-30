package com.ecommerce.demo.repositories;

import com.ecommerce.demo.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart,String> {
}
