package com.ecommerce.demo.services;

import com.ecommerce.demo.models.Cart;
import com.ecommerce.demo.models.User;
import com.ecommerce.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, CartService cartService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
    }

    public void addUser(User user) {
        if (user.getId() == null) {
            String uuid = UUID.randomUUID().toString();
            user.setId(uuid);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Cart cart = new Cart();
            cart.setId(user.getId());
            user.setCartId(cart);
        }
        if (userRepo.existsById(user.getId())) {
            throw new IllegalArgumentException("User already exists");
        } else {
            user.setCartId(user.getCartId());
            userRepo.save(user);
        }
    }

    public User getUserById(String userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException(userId + ": User ID not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User updateUser(User user) {
        User existingUser = userRepo.findByName(user.getName());
        existingUser.setPhone(user.getPhone());
        existingUser.setEmail(user.getEmail());
        existingUser.setAddress(user.getAddress());
        return userRepo.save(existingUser);
    }

    public User resetPassword(String email,String newPass){
        User existingUser = userRepo.findByEmail(email);
        existingUser.setPassword(passwordEncoder.encode(newPass));
        return userRepo.save(existingUser);
    }

    public void removeUser(String userName) {
        User user = userRepo.findByName(userName);
        if (userRepo.existsById(user.getId())) {
            userRepo.delete(user);
            cartService.removeCart(user.getId());
        } else {
            throw new IllegalArgumentException("User with username " + userName + " does not exist");
        }
    }
}

