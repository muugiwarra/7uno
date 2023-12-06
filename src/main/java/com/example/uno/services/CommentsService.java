package com.example.uno.services;

import com.example.uno.entities.Comments;
import com.example.uno.repositories.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    public Comments saveComment(Comments comment) {
        // Add any business logic/validation here
        return commentsRepository.save(comment);
    }

    // Add other methods as needed
}
