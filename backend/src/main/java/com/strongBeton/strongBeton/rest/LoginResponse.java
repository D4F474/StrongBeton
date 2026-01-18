package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.DTO.UserDTO;

public class LoginResponse {
    private String token;
    private long expiresIn;
    private UserDTO userDTO;

    public String getToken(){
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
