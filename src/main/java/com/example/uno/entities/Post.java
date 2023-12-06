package com.example.uno.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] content; // Assuming content can be binary data (e.g., image or video)


    @Column(nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    private User user;

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
    }

    public void setText(String text) {
    }

    public void setContent(byte[] bytes) {
    }

    // getters and setters
}
