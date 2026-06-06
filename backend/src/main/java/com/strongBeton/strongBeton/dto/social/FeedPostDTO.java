package com.strongBeton.strongBeton.dto.social;

import com.strongBeton.strongBeton.dto.user.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

public class FeedPostDTO {
    private Integer id;
    private UserDTO user;
    private String content;
    private LocalDateTime createdAt;
    private String type;
    private int likesCount;
    private boolean likedByMe;
    private List<FeedPostCommentDTO> comments;

    public FeedPostDTO() {
    }

    public FeedPostDTO(UserDTO user, String content, LocalDateTime createdAt, String type,
                       int likesCount, boolean likedByMe, List<FeedPostCommentDTO> comments) {
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
        this.type = type;
        this.likesCount = likesCount;
        this.likedByMe = likedByMe;
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isLikedByMe() {
        return likedByMe;
    }

    public void setLikedByMe(boolean likedByMe) {
        this.likedByMe = likedByMe;
    }

    public List<FeedPostCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<FeedPostCommentDTO> comments) {
        this.comments = comments;
    }
}
