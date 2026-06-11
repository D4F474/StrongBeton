package com.strongBeton.strongBeton.entity.social;

import com.strongBeton.strongBeton.entity.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "feed_post_comment")
public class FeedPostComment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FeedPost feedPost;

    @Column(name="content")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    public FeedPostComment() {
    }

    public FeedPostComment(User user, FeedPost feedPost, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.user = user;
        this.feedPost = feedPost;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public FeedPost getFeedPost() {
        return feedPost;
    }

    public void setFeedPost(FeedPost feedPost) {
        this.feedPost = feedPost;
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
}
