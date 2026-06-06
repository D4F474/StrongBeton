package com.strongBeton.strongBeton.entity.social;

import com.strongBeton.strongBeton.enums.FriendStatus;
import jakarta.persistence.*;

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

    public FriendShip() {
    }

    public FriendShip(int user_id, int friend_id, FriendStatus status) {
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

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", userId='" + user_id + '\'' +
                ", status=" + status +
                '}';
    }
}
