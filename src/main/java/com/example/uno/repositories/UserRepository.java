package com.example.uno.repositories;

import com.example.uno.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods can be added here if needed

    // Example:
    // User findByUsername(String username);
}

