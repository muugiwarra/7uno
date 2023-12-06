package com.example.uno.services;

import com.example.uno.entities.Post;
import com.example.uno.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post savePost(Post post) {
        // Add any business logic/validation here
        return postRepository.save(post);
    }
    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }
}
