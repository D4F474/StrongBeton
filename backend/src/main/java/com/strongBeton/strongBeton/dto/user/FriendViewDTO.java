package com.strongBeton.strongBeton.dto.user;

import com.strongBeton.strongBeton.enums.FriendStatus;

public class FriendViewDTO {
    private String friend;
    private FriendStatus status;

    public FriendViewDTO() {
    }

    public FriendViewDTO(String friend, FriendStatus status) {
        this.friend = friend;
        this.status = status;
    }

    public String getFriend() {
        return friend;
    }

    public FriendStatus getStatus() {
        return status;
    }
}
