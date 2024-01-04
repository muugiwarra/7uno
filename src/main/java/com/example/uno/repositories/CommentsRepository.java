package com.example.uno.repositories;

import com.example.uno.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByPostId(Long postId);
    // You can add custom query methods if needed
}

