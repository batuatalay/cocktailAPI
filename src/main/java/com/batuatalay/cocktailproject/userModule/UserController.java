package com.batuatalay.cocktailproject.userModule;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import java.security.SecureRandom;

@RestController

@RequestMapping("api/users")
public class UserController {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Autowired
    private UserRepository UserRepository;

    @PostMapping()
    public String createUser(@RequestBody User user) {
       return UserRepository.save(user).toString();
    }

    @GetMapping()
    public List<User> getUsers() {
        return UserRepository.findAll();
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User currentUser = UserRepository.findByUsername(user.getUsername());
        if (currentUser != null && user.getPassword().equals(currentUser.getPassword())) {
            byte[] randomBytes = new byte[24];
            secureRandom.nextBytes(randomBytes);
            String authToken =  base64Encoder.encodeToString(randomBytes);
            currentUser.setAuthToken(authToken);
            UserRepository.save(currentUser);
            return authToken;
        }
        return "Login Failed";
    }
    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authorizationHeader) {
        String[] parseToken = authorizationHeader.split(" ");
        String token = parseToken[1];
        User currentUser = UserRepository.findByauthToken(token);
        if (currentUser != null) {
            currentUser.setAuthToken(null);
            UserRepository.save(currentUser);
            return "Logged out successfully";
        } else {
            return "Authentication Bearer Token required";
        }
    }

    @GetMapping("/status/{status}")
    public List<User> getActiveUsers(@PathVariable String status) {
        return UserRepository.findByStatus(status);
    }

    public boolean loginCheck(String authorizationHeader){
        String[] parseToken = authorizationHeader.split(" ");
        String token = parseToken[1];
        if(UserRepository.findByauthToken(token) != null) {
            return true;
        }else {
            return false;
        }
    }
}
