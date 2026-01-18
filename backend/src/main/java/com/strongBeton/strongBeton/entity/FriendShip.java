package com.strongBeton.strongBeton.entity;

import com.strongBeton.strongBeton.enums.FriendStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "friendship")
public class FriendShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_friendship")
    private int id;

    @Column(name = "uuid_user", columnDefinition = "BINARY(16)")
    private UUID user_id;

    @Column(name = "friend_uuid", columnDefinition = "BINARY(16)")
    private UUID friend_id;

    @Column(name="status")
    @Enumerated(EnumType.STRING )
    private FriendStatus status;

    public FriendShip() {
    }

    public FriendShip(UUID user_id, UUID friend_id, FriendStatus status) {
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUsername() {
        return user_id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public UUID getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(UUID friend_id) {
        this.friend_id = friend_id;
    }

    public void setUsername(UUID user_id) {
        this.user_id = user_id;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
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
