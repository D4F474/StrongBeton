package com.strongBeton.strongBeton.service.security;

import com.strongBeton.strongBeton.dto.user.LoginDTO;
import com.strongBeton.strongBeton.dto.user.RegisterUserDTO;
import com.strongBeton.strongBeton.entity.user.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public User signup(RegisterUserDTO input);
    public User authenticate(LoginDTO input);
}
