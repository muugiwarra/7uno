package com.example.uno.repositories;


import com.example.uno.entities.Messages;
import com.example.uno.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long> {
    // Add this custom query method to retrieve messages between two users
    List<Messages> findBySenderAndReceiverAndSentAtIsAfterAndSentAtIsBefore(
            User sender, User receiver, LocalDateTime start, LocalDateTime end);

}

