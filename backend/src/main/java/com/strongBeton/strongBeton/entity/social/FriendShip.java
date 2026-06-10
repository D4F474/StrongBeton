package com.strongBeton.strongBeton.entity.social;

import com.strongBeton.strongBeton.enums.FriendStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendship")
public class FriendShip {

    //TODO TUKA IMA NESHTO S GETUSERNAME PROUCHI GO

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_friendship")
    private int id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "friend_id")
    private int friend_id;

    @Column(name="status")
    @Enumerated(EnumType.STRING )
    private FriendStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public FriendShip() {
    }

    public FriendShip(int user_id, int friend_id, FriendStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsername() {
        return user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public void setUsername(int user_id) {
        this.user_id = user_id;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", userId='" + user_id + '\'' +
                ", status=" + status +
                '}';
    }
}
