package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.social.FeedPost;
import com.strongBeton.strongBeton.entity.social.FeedPostLike;
import com.strongBeton.strongBeton.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeedPostLikeRepository extends JpaRepository<FeedPostLike, Integer> {
    //Намира пост на определен потребител който го е създал
    Optional<FeedPostLike> findByFeedPostAndUser(FeedPost feedPost, User managedUser);

    //изтрива пост на потребителя
    @Modifying
    @Query("DELETE FROM FeedPostLike fpl WHERE fpl.feedPost = :feedPost")
    void deleteByFeedPost(@Param("feedPost") FeedPost feedPost);
}
