package com.strongBeton.strongBeton.dto.social;

import com.strongBeton.strongBeton.dto.user.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FeedPostDTO {
    private Integer id;
    @NotBlank
    @Size(max = 300)
    private String content;
    private String type;

    private String username;
    private UUID userUuid;

    private int likesCount;
    private int commentsCount;
    private boolean likedByMe;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<FeedPostCommentDTO> comments;

    public FeedPostDTO() {
    }

    public FeedPostDTO(Integer id, String content, String type, String username, UUID userUuid, int likesCount, int commentsCount, boolean likedByMe, LocalDateTime createdAt, LocalDateTime updatedAt, List<FeedPostCommentDTO> comments) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.username = username;
        this.userUuid = userUuid;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.likedByMe = likedByMe;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.comments = comments;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public boolean isLikedByMe() {
        return likedByMe;
    }

    public void setLikedByMe(boolean likedByMe) {
        this.likedByMe = likedByMe;
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

    public List<FeedPostCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<FeedPostCommentDTO> comments) {
        this.comments = comments;
    }
}
