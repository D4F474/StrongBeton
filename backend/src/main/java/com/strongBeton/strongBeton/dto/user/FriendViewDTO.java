package com.strongBeton.strongBeton.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.strongBeton.strongBeton.enums.FriendStatus;

public class FriendViewDTO {
    private String friend;
    private FriendStatus status;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String profileImageUrl;
    public FriendViewDTO() {
    }

    public FriendViewDTO(String friend, FriendStatus status, String profileImageUrl) {
        this.friend = friend;
        this.status = status;
        this.profileImageUrl = profileImageUrl;
    }

    public String getFriend() {
        return friend;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setFriend(String friend) {
        this.friend = friend;
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
