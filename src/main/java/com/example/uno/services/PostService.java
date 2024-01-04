package com.example.uno.services;

import com.example.uno.entities.Post;
import com.example.uno.entities.User;
import com.example.uno.repositories.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private static final Logger log = LoggerFactory.getLogger(PostService.class);



    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post savePost(Post post) {
        try {
            // Add any business logic/validation here
            return postRepository.save(post);
        } catch (Exception e) {
            e.printStackTrace();
            // Log the exception or add additional error handling
            throw new RuntimeException("Failed to save post: " + e.getMessage(), e);
        }
    }

    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    public List<Post> getPostsByUser(User user) {
        return postRepository.findByUser(user);
    }
    @Transactional
    public void deletePost(Long id) {
        try {
            // Retrieve the post by ID
            Post postToDelete = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));

            // Delete the post from the repository
            postRepository.delete(postToDelete);
        } catch (Exception e) {
            // Log the exception or add additional error handling
            log.error("Failed to delete post with ID: " + id, e);
            throw new RuntimeException("Failed to delete post: " + e.getMessage(), e);
        }
    }



}
