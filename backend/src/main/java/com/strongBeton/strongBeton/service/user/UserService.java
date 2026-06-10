package com.strongBeton.strongBeton.service.user;

import com.strongBeton.strongBeton.dto.user.UserDTO;
import com.strongBeton.strongBeton.dto.user.UserUpdateDTO;
import com.strongBeton.strongBeton.entity.user.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public UserDTO loadUserDataByEmail(String email);
    public List<User> allUsers();
    public UserDTO findUserByUsername(String username);
    public UserDTO updateUser(UUID UUID, UserDTO userUpdateDTO);
}
