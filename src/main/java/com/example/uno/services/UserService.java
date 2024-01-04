package com.example.uno.services;

import com.example.uno.entities.User;
import com.example.uno.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public User fetchUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public Pair<User, User> fetchUsersByUsernames(String senderUsername, String receiverUsername) {
        User sender = fetchUserByUsername(senderUsername);
        User receiver = fetchUserByUsername(receiverUsername);

        if (sender == null || receiver == null) {
            // Handle the case where sender or receiver is not found
            System.out.println("Sender: " + senderUsername + ", Receiver: " + receiverUsername);
            return null;
        }

        return Pair.of(sender, receiver);
    }

    public boolean authenticateUser(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails;
        try {
            userDetails = customUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return false;
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            String token = generateJwtToken(userDetails.getUsername());
            setAuthenticationCookie(response, token);
            return true;
        } else {
            return false;
        }
    }

    private String generateJwtToken(String username) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        return Jwts.builder()
                .setSubject(username)
                .signWith(key)
                .compact();
    }

    private void setAuthenticationCookie(HttpServletResponse response, String token) {
        System.out.println("Setting authentication cookie with token: " + token);
        Cookie cookie = new Cookie("authenticationToken", token);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);
        System.out.println("Authentication cookie set successfully");
    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve user from the database by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Assuming your User entity has fields like username, password, and roles
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                // Add more authorities/roles as needed
        );
    }
}

