package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.FriendView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendViewRepository extends JpaRepository<FriendView, Integer> {

    @Query(value = "SELECT id, friend, status FROM show_friend_list_view WHERE username = :username", nativeQuery = true)
    List<FriendView> findAllFriendsVisual(@Param("username") String username);

}
