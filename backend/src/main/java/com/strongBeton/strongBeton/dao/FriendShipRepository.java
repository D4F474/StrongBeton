package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.FriendShip;
import com.strongBeton.strongBeton.enums.FriendStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendShipRepository extends JpaRepository<FriendShip, Integer> {

    @Query(value = "SELECT * FROM friendship WHERE user_id = :userId", nativeQuery = true)
    Optional<List<FriendShip>> findByUserId(@Param("userId") int userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO friendship(user_id, friend_id, status) VALUES (:userId, :friendId, 1)", nativeQuery = true)
    void sendingFriendRequest(@Param("userId")int userId,@Param("friendId") int friendId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO friendship(user_id, friend_id, status) VALUES (:friendId, :userId, 4)", nativeQuery = true)
    void receiveTheRequest(@Param("friendId") int friendId, @Param("userId")int userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE friendship SET status = :statusD WHERE user_id = :userId AND friend_id = :friendId", nativeQuery = true)
    void acceptFriendRequest(@Param("userId") int userId, @Param("friendId") int friendId, @Param("statusD")FriendStatus statusD);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM friendship WHERE user_id = :userId AND friend_id = :friendId", nativeQuery = true)
    void deleteFriendRequest(@Param("userId")int userId, @Param("friendId") int friendId);
}
