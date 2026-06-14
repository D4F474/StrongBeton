package com.strongBeton.strongBeton.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserStatusDTO {

    String username;

    String status;

    String profileImageUrl;

    public UserStatusDTO(String username, String status, String profileImageUrl) {
        this.username = username;
        this.status = status;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
