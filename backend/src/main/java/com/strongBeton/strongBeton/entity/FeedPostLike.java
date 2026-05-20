package com.strongBeton.strongBeton.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "feed_post_like")
public class FeedPostLike {
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

    public FeedPostLike() {
    }

    public FeedPostLike(User user, FeedPost feedPost) {
        this.user = user;
        this.feedPost = feedPost;
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
}
