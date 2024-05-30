package com.ecommerce.demo.controllers;

import com.ecommerce.demo.models.User;

import com.ecommerce.demo.repositories.CartRepo;
import com.ecommerce.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    CartRepo cartRepo;

    @PostMapping(value = "/add")
    public ResponseEntity<User> addUser(@RequestBody User user){
        try{
            userService.addUser(user);
            log.info("User added Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }catch(Exception e){
            log.error("Error adding user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping(value = "/get/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId){
        try {
            userService.getUserById(userId);
            log.info("Retrieved user by ID:{}",userId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (RuntimeException e) {
            log.warn("An Error occurred : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userService.getAllUsers();
        if(users.isEmpty()){
            log.warn("User repository is Empty");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(users);
        }
        log.info("Retrieved all users :{}",users);
        return ResponseEntity.status(HttpStatus.OK).body(users);

    }

    @PutMapping(value = "/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        try {
            User updatedUser = userService.updateUser(user);
            log.info("User updated successfully: {}", updatedUser);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (IllegalArgumentException e) {
            log.warn("An Error occurred : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
        }
    }

    @PutMapping(value = "/reset-password")
    public ResponseEntity<User> resetPassword(@RequestBody String email,@RequestBody String newPass){
        try{
            User updatedUser = userService.resetPassword(email,newPass);
            log.info("Password successfully reset: {}", updatedUser.getName());
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        }catch (Exception e){
            log.warn("An Error occurred : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);
        }
    }


    @DeleteMapping(value = "/delete/{userName}")
    public ResponseEntity<String> removeUser(@PathVariable String userName){
        try {
            userService.removeUser(userName);
            log.info("User deleted successfully: {}", userName);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
        } catch(Exception e) {
            log.warn("An Error occurred : {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
