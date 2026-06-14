package com.strongBeton.strongBeton.dto.social;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class FeedPostCommentDTO {

    private Integer id;
    @NotBlank
    @Size(max = 1000)
    private String content;

    private String username;
    private UUID userUuid;
    private String profilePhotoUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FeedPostCommentDTO() {
    }

    public FeedPostCommentDTO(Integer id, String content, String username,
                              UUID userUuid, LocalDateTime createdAt, LocalDateTime updatedAt,
                              String profilePhotoUrl) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.userUuid = userUuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
}
