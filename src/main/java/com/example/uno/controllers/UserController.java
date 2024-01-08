package com.example.uno.controllers;

import com.example.uno.entities.User;
import com.example.uno.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {


    private final UserService userService;
    private HttpServletRequest request;


    @Autowired
    public UserController(

            UserService userService) {

        this.userService = userService;


    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        System.out.println("Received signup request: " + user.getUsername());


        // Call the service to handle user signup
        userService.saveUser(user);

        // Respond with a success message
        return ResponseEntity.ok("User signed up successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpServletResponse response) {
        System.out.println("Received login request: " + user.getUsername());

        // Check if the provided username and password match a user in the system
        if (userService.authenticateUser(user.getUsername(), user.getPassword(), request, response)) {
            // Respond with a success message
            return ResponseEntity.ok("Login successful!");
        } else {
            // Respond with an error message
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(Principal principal) {
        if (principal != null) {
            // Principal contains the currently authenticated user's information
            String username = principal.getName();

            // You can fetch additional user details using the username
            User user = userService.getUserByUsername(username);

            if (user != null) {
                // Respond with the user's profile information
                String userProfile = "Username: " + user.getUsername() + ", Full Name: " + user.getFullName() + ", Email: " + user.getEmail();
                return ResponseEntity.ok(userProfile);
            } else {
                // User not found
                return ResponseEntity.status(404).body("User not found");
            }
        } else {
            // No authenticated user
            return ResponseEntity.status(401).body("Not authenticated");
        }
    }





}



    



