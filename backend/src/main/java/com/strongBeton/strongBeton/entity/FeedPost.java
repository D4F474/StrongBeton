package com.strongBeton.strongBeton.entity;

import com.strongBeton.strongBeton.enums.PostType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "feed_post")
public class FeedPost {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "feedPost", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FeedPostComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "feedPost", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FeedPostLike> likes = new ArrayList<>();

    @Column(name="type")
    @Enumerated(EnumType.STRING)
    private PostType postType;
    public FeedPost() {
    }

    public FeedPost(User user, String content, LocalDateTime createdAt, LocalDateTime updatedAt, PostType postType) {
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.postType = postType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }
}
