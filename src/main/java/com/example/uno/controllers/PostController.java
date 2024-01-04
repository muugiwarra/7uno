package com.example.uno.controllers;

import com.example.uno.entities.Post;
import com.example.uno.entities.User;
import com.example.uno.services.PostService;
import com.example.uno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.uno.repositories.PostRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final PostRepository postRepository;


    @Autowired
    public PostController(PostService postService, UserService userService, PostRepository postRepository ) {
        this.postService = postService;
        this.userService = userService;
        this.postRepository = postRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "username") String username) {
        if (file != null && !file.isEmpty()) {
            System.out.println("File Size: " + file.getSize());

            try {
                byte[] fileContent = file.getBytes();
                System.out.println("File Content: " + Arrays.toString(fileContent));

                // Retrieve the user by username (you may use a different parameter)
                User user = userService.getUserByUsername(username);

                // Create a new Post
                Post post = new Post();
                post.setContent(fileContent);

                // Set the user for the post
                post.setUser(user);

                // Optionally, set text content if available
                if (text != null && !text.isEmpty()) {
                    post.setText(text);
                }

                // Save the post using the PostService
                Post savedPost = postService.savePost(post);

                return ResponseEntity.ok("File uploaded successfully. Post saved with ID: " + savedPost.getId());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Failed to process the file.");
            }
        } else {
            return ResponseEntity.badRequest().body("File is empty.");
        }
    }
    @GetMapping("/retrieve")
    public ResponseEntity<List<Post>> retrievePostsByUsername(
            @RequestParam("username") String username,
            @RequestParam(name = "postId", required = false) Long postId) {
        User user = userService.getUserByUsername(username);

        // Check if postId is provided
        if (postId != null) {
            // If postId is provided, retrieve only the post with that specific ID
            Optional<Post> post = postService.getPostById(postId);
            if (post.isPresent() && post.get().getUser() != null && post.get().getUser().equals(user)) {
                // Ensure the post belongs to the specified user
                return ResponseEntity.ok(Collections.singletonList(post.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            // If postId is not provided, retrieve all posts for the given user
            List<Post> posts = postService.getPostsByUser(user);
            return ResponseEntity.ok(posts);
        }
    }



    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestParam(name = "text") String text) {
        try {
            // Create a new Post
            Post post = new Post();
            post.setText(text);

            // Save the post using the PostService
            Post savedPost = postService.savePost(post);

            // Respond with the saved post
            return ResponseEntity.ok(savedPost);
        } catch (Exception e) {
            // Handle exceptions, e.g., database errors
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok("Post deleted successfully.");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(404).body("Post not found.");
        } catch (Exception e) {
            e.printStackTrace();  // Add this line
            return ResponseEntity.status(500).body("Failed to delete post: " + e.getMessage());
        }
    }







    @PutMapping("/update/{postId}")
    public ResponseEntity<String> updatePost(
            @PathVariable Long postId,
            @RequestParam(name = "text") String newText) {
        Post existingPost = postService.getPostById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        existingPost.setText(newText);
        postService.savePost(existingPost);

        return ResponseEntity.ok("Post updated successfully.");
    }
}
