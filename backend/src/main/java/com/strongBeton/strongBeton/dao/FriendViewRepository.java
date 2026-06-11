<<<<<<< HEAD
package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.social.FriendView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendViewRepository extends JpaRepository<FriendView, Integer> {

    @Query(value = "SELECT id, friend, status FROM show_friend_list_view WHERE username = :username", nativeQuery = true)
    Optional<List<FriendView>> findAllFriendsVisual(@Param("username") String username);

}
=======
package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.social.FriendView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendViewRepository extends JpaRepository<FriendView, Integer> {

    @Query(value = "SELECT id, friend, status FROM show_friend_list_view WHERE username = :username", nativeQuery = true)
    Optional<List<FriendView>> findAllFriendsVisual(@Param("username") String username);

}
>>>>>>> fda96bb (Add Dockerized backend and MySQL setup)
