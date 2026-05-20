package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.FeedPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface FeedPostRepository extends JpaRepository<FeedPost, Integer> {
    Collection<Object> findByUserIdInOrderByCreatedAtDesc(List<Integer> friendIds);
}
