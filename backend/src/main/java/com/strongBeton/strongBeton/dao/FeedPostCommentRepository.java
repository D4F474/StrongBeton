package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.social.FeedPost;
import com.strongBeton.strongBeton.entity.social.FeedPostComment;
import com.strongBeton.strongBeton.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeedPostCommentRepository extends JpaRepository<FeedPostComment, Integer> {
    //Намира пост и неговия създател (потребител)
    Optional<FeedPostComment> findByFeedPostAndUser(FeedPost feedPost, User managedUser);

    //Изтрива пост на потребител
    @Modifying
    @Query("DELETE FROM FeedPostLike fpl WHERE fpl.feedPost = :feedPost")
    void deleteByFeedPost(@Param("feedPost") FeedPost feedPost);
}
