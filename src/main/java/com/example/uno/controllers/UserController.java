package com.example.uno.controllers;

import com.example.uno.entities.User;
import com.example.uno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")

public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
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

}
