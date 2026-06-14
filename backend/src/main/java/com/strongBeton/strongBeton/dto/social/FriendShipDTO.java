package com.strongBeton.strongBeton.dto.social;

import com.strongBeton.strongBeton.enums.FriendStatus;

public class FriendShipDTO {
    private int id;
    private int user_id;
    private int friend_id;
    private FriendStatus status;
    private String profileImageUrl;
    public FriendShipDTO() {
    }

    public FriendShipDTO(int id, int user_id, int friend_id, FriendStatus status, String profileImageUrl) {
        this.id = id;
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.status = status;
        this.profileImageUrl = profileImageUrl;
    }

    public FriendShipDTO(int id, int user_id, int friend_id, FriendStatus status) {
        this.id = id;
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

    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
