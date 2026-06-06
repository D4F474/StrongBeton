package com.strongBeton.strongBeton.entity.social;

import com.strongBeton.strongBeton.enums.FriendStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "show_friend_list_view")
public class FriendView {

    @Column(name = "id")
    @Id
    private int id;

    @Column(name = "friend")
    private String friend;

    @Column(name = "status")
    @Enumerated(EnumType.STRING )
    private FriendStatus status;

    public FriendView() {
    }

    public FriendView(String friend, FriendStatus status) {
        this.friend = friend;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FriendView{" +
                "id=" + id +
                ", friend='" + friend + '\'' +
                ", status=" + status +
                '}';
    }
}
