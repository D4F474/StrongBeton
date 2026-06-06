package com.strongBeton.strongBeton.service.security;

import com.strongBeton.strongBeton.dto.user.LoginDTO;
import com.strongBeton.strongBeton.dto.user.RegisterUserDTO;
import com.strongBeton.strongBeton.dao.RoleRepository;
import com.strongBeton.strongBeton.dao.UserRepository;
import com.strongBeton.strongBeton.entity.user.AdditionalInfo;
import com.strongBeton.strongBeton.entity.user.City;
import com.strongBeton.strongBeton.entity.user.Role;
import com.strongBeton.strongBeton.entity.user.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passswordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
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
        Role role;
        user.setUuid(UUID.randomUUID());
        System.out.println(user.getId());
        user.setUsername(input.getUsername());
        user.setPassword(passswordEncoder.encode(input.getPassword()));
        user.setEmail(input.getEmail());

        additionalInfo.setFirstName(input.getFirstName());
        additionalInfo.setLastName(input.getLastName());
        additionalInfo.setStreetInfo(input.getStreetName());
        additionalInfo.setCity(new City(input.getCityName()));
        additionalInfo.setCm(input.getCm());
        additionalInfo.setKg(input.getKg());
        additionalInfo.setBornDate(input.getBornDate());
        additionalInfo.setGender(input.isGender());

        Optional<Role> optional = this.roleRepository.findByRoleName("Beton");
        if(optional.isPresent()){
            role = optional.get();
        }else{
            role = new Role("Beton");
            this.roleRepository.save(role);
        }

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
