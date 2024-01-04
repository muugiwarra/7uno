package com.example.uno.controllers;

import com.example.uno.entities.Messages;
import com.example.uno.entities.User;
import com.example.uno.services.MessageService;
import com.example.uno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessagesController {

    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessagesController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/send")
    public ResponseEntity<Messages> sendMessage(
            @RequestParam(name = "senderusername") String senderusername,
            @RequestParam(name = "receiverusername") String receiverusername,
            @RequestParam(name = "content") String content) {
        System.out.println("sendMessage method called");

        // Retrieve sender and receiver users using fetchUserByUsername
        User sender = userService.fetchUserByUsername(senderusername);
        User receiver = userService.fetchUserByUsername(receiverusername);

        System.out.println("Sender: " + sender);
        System.out.println("Receiver: " + receiver);

        if (sender == null || receiver == null) {
            // Handle the case where sender or receiver is not found
            return ResponseEntity.status(404).body(null);
        }

        // Create a new message
        Messages message = new Messages();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());

        // Save the message using the MessageService
        Messages savedMessage = messageService.saveMessage(message);

        // Respond with the saved message
        return ResponseEntity.ok(savedMessage);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        // Check if the message with the given ID exists
        Optional<Messages> optionalMessage = messageService.getMessageById(id);
        if (optionalMessage.isPresent()) {
            // Message found, delete it
            messageService.deleteMessage(id);
            return ResponseEntity.ok("Message deleted successfully.");
        } else {
            // Message not found
            return ResponseEntity.status(404).body("Message not found with ID: " + id);
        }
    }
    @GetMapping("/conversation")
    public ResponseEntity<List<Messages>> getConversation(
            @RequestParam(name = "user1") String user1,
            @RequestParam(name = "user2") String user2) {
        // Retrieve users by their usernames
        User sender = userService.fetchUserByUsername(user1);
        User receiver = userService.fetchUserByUsername(user2);

        if (sender == null || receiver == null) {
            // Handle the case where one or both users are not found
            return ResponseEntity.status(404).body(Collections.emptyList());
        }

        // Retrieve all messages between the two users
        List<Messages> messages = messageService.getMessagesBetweenUsers(sender, receiver);

        return ResponseEntity.ok(messages);
    }
}


// Additional controller methods for handling messages

