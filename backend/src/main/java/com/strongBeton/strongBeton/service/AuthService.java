package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.LoginDTO;
import com.strongBeton.strongBeton.DTO.RegisterUserDTO;
import com.strongBeton.strongBeton.dao.RoleRepository;
import com.strongBeton.strongBeton.dao.UserRepository;
import com.strongBeton.strongBeton.entity.AdditionalInfo;
import com.strongBeton.strongBeton.entity.City;
import com.strongBeton.strongBeton.entity.Role;
import com.strongBeton.strongBeton.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AuthService {
    public User signup(RegisterUserDTO input);
    public User authenticate(LoginDTO input);
}
