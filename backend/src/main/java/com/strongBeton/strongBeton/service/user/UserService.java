package com.strongBeton.strongBeton.service.user;

import com.strongBeton.strongBeton.dto.user.UserDTO;
import com.strongBeton.strongBeton.entity.user.User;

import java.util.List;

public interface UserService {
    public UserDTO loadUserDataByEmail(String email);
    public List<User> allUsers();
    public UserDTO findUserByUsername(String username);
}
