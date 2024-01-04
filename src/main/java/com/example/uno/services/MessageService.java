package com.example.uno.services;


import com.example.uno.entities.Messages;
import com.example.uno.entities.User;
import com.example.uno.repositories.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessagesRepository messagesRepository;

    @Autowired
    public MessageService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public Messages saveMessage(Messages message) {
        return messagesRepository.save(message);
    }

    public Optional<Messages> getMessageById(Long id) {
        return messagesRepository.findById(id);
    }

    public void deleteMessage(Long id) {
        messagesRepository.deleteById(id);
    }

    public List<Messages> getMessagesBetweenUsers(User user1, User user2) {
        // Define a time range, you can customize this based on your requirements
        LocalDateTime start = LocalDateTime.MIN;
        LocalDateTime end = LocalDateTime.now();

        return messagesRepository.findBySenderAndReceiverAndSentAtIsAfterAndSentAtIsBefore(
                user1, user2, start, end);
    }
}
