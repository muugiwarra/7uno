package com.example.uno.controllers;

import com.example.uno.entities.Post;
import com.example.uno.services.PostService;
import com.example.uno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name = "text", required = false) String text) {
        // You will receive the file (image or video) and text from the frontend

        // Assume that the user is already authenticated (no need for additional authentication)

        // Create a new Post
        Post post = new Post();

        // Set the content based on the received file (image or video)
        if (file != null && !file.isEmpty()) {
            try {
                post.setContent(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        } else if (text != null && !text.isEmpty()) {
            // Set the text content
            post.setContent(text.getBytes());
        }

        // Save the post using the PostService
        Post savedPost = postService.savePost(post);

        // Respond with the saved post
        return ResponseEntity.ok(savedPost);
    }
}
