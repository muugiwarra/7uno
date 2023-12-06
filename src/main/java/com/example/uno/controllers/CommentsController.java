package com.example.uno.controllers;

import com.example.uno.entities.Comments;
import com.example.uno.entities.Post;
import com.example.uno.entities.User;
import com.example.uno.services.CommentsService;
import com.example.uno.services.PostService;
import com.example.uno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentsController {

    private final CommentsService commentsService;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public CommentsController(CommentsService commentsService, UserService userService, PostService postService) {
        this.commentsService = commentsService;
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<Comments> createComment(
            @RequestParam(name = "text") String text,
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "postId") Long postId) {
        // You will receive the comment text, userId, and postId from the frontend

        // Retrieve the authenticated user or handle user authentication
        Optional<User> authenticatedUser = userService.getUserById(userId);

        // Retrieve the post based on postId
        Optional<Post> post = postService.getPostById(postId);

        // Create a new Comment
        Comments comment = new Comments();
        comment.setText(text);
        comment.setUser(authenticatedUser);
        comment.setPost(post);

        // Save the comment using the CommentsService
        Comments savedComment = commentsService.saveComment(comment);

        // Respond with the saved comment
        return ResponseEntity.ok(savedComment);
    }
}
