package com.strongBeton.strongBeton.dto.social;

import com.strongBeton.strongBeton.enums.FriendStatus;

public class FriendViewDTO {

    private int id;
    private String friend;
    private FriendStatus status;

    public FriendViewDTO() {
    }

    public FriendViewDTO(int id, String friend, FriendStatus status) {
        this.id = id;
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
}
