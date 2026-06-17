package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.social.FeedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface FeedPostRepository extends JpaRepository<FeedPost, Integer> {
    Collection<Object> findByUserIdInOrderByCreatedAtDesc(List<Integer> friendIds);

    // Намира постовете на подадените потребители, подредени от най-новите към най-старите.
    @Query("""
        SELECT p
        FROM FeedPost p
        WHERE p.user.id IN :userIds
        AND p.hidden = false
        ORDER BY p.createdAt DESC
    """)
    List<FeedPost> findByUserIdsOrderByCreatedAtDesc(@Param("userIds") List<Integer> userIds);
}
