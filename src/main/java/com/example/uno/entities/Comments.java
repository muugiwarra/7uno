package com.example.uno.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post post;



    public void setUser(Optional<User> authenticatedUser) {
    }

    public void setPost(Optional<Post> post) {
    }


    @PrePersist
    protected void onCreate() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}


