package com.example.uno.services;

import com.example.uno.entities.Comments;
import com.example.uno.repositories.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentsService {

    private final CommentsRepository commentsRepository;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    public Comments saveComment(Comments comment) {
        try {
            // Add any business logic/validation here
            return commentsRepository.save(comment);
        } catch (Exception e) {
            // Log the exception or add additional error handling
            throw new RuntimeException("Failed to save comment: " + e.getMessage(), e);
        }
    }

    public Optional<Comments> getCommentById(Long commentId) {
        return commentsRepository.findById(commentId);
    }

    public void deleteComment(Long commentId) {
        try {
            // Retrieve the comment by ID
            Comments commentToDelete = commentsRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));

            // Delete the comment from the repository
            commentsRepository.delete(commentToDelete);
        } catch (Exception e) {
            // Log the exception or add additional error handling
            throw new RuntimeException("Failed to delete comment: " + e.getMessage(), e);
        }
    }

    public List<Comments> getCommentsByPostId(Long postId) {
        // Implement the logic to retrieve comments by postId
        return commentsRepository.findByPostId(postId);
    }
}

