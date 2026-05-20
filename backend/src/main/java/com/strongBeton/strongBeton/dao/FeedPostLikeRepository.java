package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.FeedPost;
import com.strongBeton.strongBeton.entity.FeedPostLike;
import com.strongBeton.strongBeton.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeedPostLikeRepository extends JpaRepository<FeedPostLike, Integer> {
    Optional<FeedPostLike> findByFeedPostAndUser(FeedPost feedPost, User managedUser);

    @Modifying
    @Query("DELETE FROM FeedPostLike fpl WHERE fpl.feedPost = :feedPost")
    void deleteByFeedPost(@Param("feedPost") FeedPost feedPost);
}
