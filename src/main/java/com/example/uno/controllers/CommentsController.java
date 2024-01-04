package com.example.uno.controllers;

import com.example.uno.entities.Comments;
import com.example.uno.entities.Post;
import com.example.uno.entities.User;
import com.example.uno.services.CommentsService;
import com.example.uno.services.PostService;
import com.example.uno.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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
            @RequestParam(name = "username") String username,
            @RequestParam(name = "postId") Long postId) {
        // Retrieve the authenticated user or handle user authentication
        User user = userService.getUserByUsername(username);

        if (user == null) {
            // Handle the case where the user with the given username is not found
            return ResponseEntity.status(404).body(null);
        }

        // Retrieve the post based on postId
        Optional<Post> post = postService.getPostById(postId);

        if (post.isEmpty()) {
            // Handle the case where the post with the given postId is not found
            return ResponseEntity.status(404).body(null);
        }

        // Create a new Comment
        Comments comment = new Comments();
        comment.setText(text); // Set the text property
        comment.setUser(user);
        comment.setPost(post.get()); // Use get() to retrieve the actual Post instance

        // Save the comment using the CommentsService
        Comments savedComment = commentsService.saveComment(comment);

        // Respond with the saved comment
        return ResponseEntity.ok(savedComment);
    }



    @PutMapping("/update/{commentId}")
    public ResponseEntity<Comments> updateComment(
            @PathVariable Long commentId,
            @RequestParam(name = "text") String newText) {
        // Retrieve the comment by commentId
        Optional<Comments> existingComment = commentsService.getCommentById(commentId);
        if (existingComment.isPresent()) {
            // Update the text of the comment
            existingComment.get().setText(newText);

            // Save the updated comment
            Comments updatedComment = commentsService.saveComment(existingComment.get());

            // Respond with the updated comment
            return ResponseEntity.ok(updatedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        try {
            // Attempt to delete the comment by commentId
            commentsService.deleteComment(commentId);
            return ResponseEntity.ok("Comment deleted successfully.");
        } catch (EmptyResultDataAccessException e) {
            // Handle case where comment is not found
            return ResponseEntity.status(404).body("Comment not found.");
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(500).body("Failed to delete comment: " + e.getMessage());
        }
    }

    @GetMapping("/retrieve/{postId}")
    public ResponseEntity<List<Comments>> retrieveCommentsByPostId(@PathVariable Long postId) {
        // Retrieve comments based on the postId
        List<Comments> comments = commentsService.getCommentsByPostId(postId);

        // Respond with the retrieved comments
        return ResponseEntity.ok(comments);
    }
}
