package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.UserDTO;
import com.strongBeton.strongBeton.dao.UserRepository;
import com.strongBeton.strongBeton.entity.User;

import java.util.List;

public interface UserService {
    public UserDTO loadUserDataByEmail(String email);
    public List<User> allUsers();
    public UserDTO findUserByUsername(String username);
}
