package com.strongBeton.strongBeton.service.security;

import com.strongBeton.strongBeton.dao.KGLogRepository;
import com.strongBeton.strongBeton.dto.user.LoginDTO;
import com.strongBeton.strongBeton.dto.user.RegisterUserDTO;
import com.strongBeton.strongBeton.dao.RoleRepository;
import com.strongBeton.strongBeton.dao.UserRepository;
import com.strongBeton.strongBeton.dto.user.UserDTO;
import com.strongBeton.strongBeton.entity.user.AdditionalInfo;
import com.strongBeton.strongBeton.entity.user.KGLogs;
import com.strongBeton.strongBeton.entity.user.Role;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.service.EmailService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final KGLogRepository kgLogRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passswordEncoder,
                           AuthenticationManager authenticationManager,
                           RoleRepository roleRepository,
                           KGLogRepository kgLogRepository,
                           ModelMapper modelMapper,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.passswordEncoder = passswordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.kgLogRepository = kgLogRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @Transactional
    public UserDTO signup(RegisterUserDTO input){
        User user = new User();
        UserDTO userDTO = new UserDTO();
        AdditionalInfo additionalInfo = new AdditionalInfo();
        Role role;

        if (userRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        if (userRepository.findByUsername(input.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already in use.");
        }

        user.setUuid(UUID.randomUUID());
        user.setUsername(input.getUsername());
        user.setPassword(passswordEncoder.encode(input.getPassword()));
        user.setEmail(input.getEmail());


        additionalInfo.setCm(input.getCm());
        additionalInfo.setBornDate(input.getBornDate());
        additionalInfo.setGender(input.getGender());
        if (input.getKg() <= 30) {
            throw new IllegalArgumentException("Body weight must be greater than 30 kg.");
        }

        KGLogs kgLogs = new KGLogs(
                input.getKg(),
                user,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Optional<Role> optional = this.roleRepository.findByRoleName("Beton");
        if(optional.isPresent()){
            role = optional.get();
        }else{
            role = new Role("Beton");
            this.roleRepository.save(role);
        }

        user.setAdditionalInfo(additionalInfo);
        user.setRole(role);
        userRepository.save(user);
        kgLogRepository.save(kgLogs);
        sendWelcomeEmail(user);

        userDTO.setId(user.getUuid());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(role.getRoleName());
        userDTO.setCm(input.getCm());
        userDTO.setBornDate(input.getBornDate());
        userDTO.setKg(kgLogs.getKg());


        return userDTO;
    }

    @Transactional
    public User authenticate(LoginDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    private void sendWelcomeEmail(User user) {
        try {
            emailService.sendEmail(
                    user.getEmail(),
                    "Welcome to StrongBeton",
                    "Welcome, " + user.getUsername() + "!\n\n"
                            + "Your StrongBeton account was created successfully."
            );
        } catch (Exception ex) {
            LOGGER.warn("Welcome email failed for user {}", user.getId(), ex);
        }
    }

}
