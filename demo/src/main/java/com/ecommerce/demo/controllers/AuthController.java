package com.ecommerce.demo.controllers;

import com.ecommerce.demo.models.JwtRequest;
import com.ecommerce.demo.models.JwtResponse;
import com.ecommerce.demo.models.User;
import com.ecommerce.demo.security.JwtHelper;
import com.ecommerce.demo.services.EmailService;
import com.ecommerce.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    // Store OTPs temporarily
    private Map<String, String> otpStorage = new HashMap<>();

    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        this.doAuthenticate(request.getName(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getName());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .name(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String username,String password){
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,password);
        try{
            manager.authenticate(authentication);
        }catch (Exception e){
            throw new RuntimeException("Invalid user name or password...");
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOTP(@RequestParam String email) {
        try{
            String otp = emailService.sendOTP(email);
            otpStorage.put(email, otp); // Store OTP with email as key
            log.info("OTP sent Succesfully");
            return ResponseEntity.ok("OTP sent successfully!");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam String otp, @RequestParam String newPass) {
        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            log.info("OTP verified Successfully");
            User updatedUser = userService.resetPassword(email, newPass);
            return ResponseEntity.ok().body(updatedUser.getName());
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP!");
        }
    }


    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

}
