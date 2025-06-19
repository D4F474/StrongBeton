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
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passswordEncoder;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passswordEncoder,
                       AuthenticationManager authenticationManager,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passswordEncoder = passswordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public User signup(RegisterUserDTO input){
        User user = new User();
        AdditionalInfo additionalInfo = new AdditionalInfo();
        Role role = new Role();
        //user
        user.setUsername(input.getUsername());
        user.setPassword(passswordEncoder.encode(input.getPassword()));
        user.setEmail(input.getEmail());

        //additional
        additionalInfo.setFirstName(input.getFirstName());
        additionalInfo.setLastName(input.getLastName());
        additionalInfo.setStreetInfo(input.getStreetName());
        additionalInfo.setCity(new City(input.getCityName()));
        additionalInfo.setCm(input.getCm());
        additionalInfo.setKg(input.getKg());
        additionalInfo.setBornDate(input.getBornDate());
        additionalInfo.setGender(input.isGender());

        //role
        Optional<Role> optional = this.roleRepository.findByRoleName("Beton");
        if(optional.isPresent()){
            role = optional.get();
        }
        //addToUser
        user.setAdditionalInfo(additionalInfo);
        user.setRole(role);
    return userRepository.save(user);
    }

    @Transactional
    public User authenticate(LoginDTO input){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

       return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }




}
