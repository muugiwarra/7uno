package com.example.uno.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LongBLOB")
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
        // You may implement text handling logic here if needed
    }

    public void setContent(byte[] bytes) {
        this.content = bytes;
    }

    public byte[] getContent() {
        return content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    // getters and setters
}
