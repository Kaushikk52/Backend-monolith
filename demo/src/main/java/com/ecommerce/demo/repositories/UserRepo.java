package com.ecommerce.demo.repositories;

import com.ecommerce.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,String> {
    User findByName(String userName);

    User findByEmail(String userEmail);
}
