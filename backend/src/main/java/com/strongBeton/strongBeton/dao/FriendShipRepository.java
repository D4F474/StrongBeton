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

    @Query(value = "SELECT * FROM friendship WHERE uuid_user = :userId", nativeQuery = true)
    Optional<List<FriendShip>> findByUserId(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO friendship(uuid_user, friend_uuid, status) VALUES (:userId, :friendId, 1)", nativeQuery = true)
    void sendingFriendRequest(@Param("userId")UUID userId,@Param("friendId") UUID friendId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO friendship(uuid_user, friend_uuid, status) VALUES (:friendId, :userId, 4)", nativeQuery = true)
    void receiveTheRequest(@Param("friendId") UUID friendId, @Param("userId")UUID userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE friendship SET status = :statusD WHERE uuid_user = :userId AND friend_uuid = :friendId", nativeQuery = true)
    void acceptFriendRequest(@Param("userId") byte[] userId, @Param("friendId") byte[] friendId, @Param("statusD")FriendStatus statusD);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM friendship WHERE uuid_user = :userId AND friend_uuid = :friendId", nativeQuery = true)
    void deleteFriendRequest(@Param("userId")UUID userId, @Param("friendId") UUID friendId);
}
