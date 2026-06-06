package com.strongBeton.strongBeton.dto.social;

import java.time.LocalDateTime;

public class FeedPostCommentDTO {

    private int id;
    private String username;
    private String content;
    private LocalDateTime createdAt;

    public FeedPostCommentDTO() {
    }

    public FeedPostCommentDTO(String username, String content, LocalDateTime createdAt) {
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
