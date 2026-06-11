<<<<<<< HEAD
package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.user.LoginDTO;
import com.strongBeton.strongBeton.dto.user.LoginResponse;
import com.strongBeton.strongBeton.dto.user.RegisterUserDTO;
import com.strongBeton.strongBeton.dto.user.UserDTO;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.service.security.AuthService;
import com.strongBeton.strongBeton.service.ImageService;
import com.strongBeton.strongBeton.service.security.JwtService;
import com.strongBeton.strongBeton.service.user.UserService;
=======
package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.user.LoginDTO;
import com.strongBeton.strongBeton.dto.user.LoginResponse;
import com.strongBeton.strongBeton.dto.user.RegisterUserDTO;
import com.strongBeton.strongBeton.dto.user.UserDTO;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.service.security.AuthService;
import com.strongBeton.strongBeton.service.ImageService;
import com.strongBeton.strongBeton.service.security.JwtService;
import com.strongBeton.strongBeton.service.user.UserService;
>>>>>>> fda96bb (Add Dockerized backend and MySQL setup)
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD

@CrossOrigin
@RequestMapping("/auth")
@RestController
=======

@CrossOrigin
@RequestMapping("/auth")
@RestController
>>>>>>> fda96bb (Add Dockerized backend and MySQL setup)
public class AuthRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRestController.class);

    private final JwtService jwtService;
<<<<<<< HEAD

    private final AuthService authService;
    private final UserService userService;
    private final ImageService imageService;

    @Autowired
    public AuthRestController(JwtService jwtService,
                              AuthService authService,
                              UserService userService,
                              ImageService imageService) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.userService = userService;
        this.imageService = imageService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> register (@Valid @RequestBody RegisterUserDTO registerUserDTO){
        UserDTO registeredUser = authService.signup(registerUserDTO);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginDTO loginUserDto) {
        try {
            User authenticatedUser = authService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());

            UserDTO userDTO = userService.loadUserDataByEmail(authenticatedUser.getEmail());
            imageService.getProfileImage(authenticatedUser.getId()).ifPresent(photo ->{
                userDTO.setProfilePhotoUrl(photo.getPhotoUrl());
            });

            loginResponse.setUserDTO(userDTO);

            return ResponseEntity.ok(loginResponse);
=======

    private final AuthService authService;
    private final UserService userService;
    private final ImageService imageService;

    @Autowired
    public AuthRestController(JwtService jwtService,
                              AuthService authService,
                              UserService userService,
                              ImageService imageService) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.userService = userService;
        this.imageService = imageService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> register (@Valid @RequestBody RegisterUserDTO registerUserDTO){
        UserDTO registeredUser = authService.signup(registerUserDTO);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginDTO loginUserDto) {
        try {
            User authenticatedUser = authService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());

            UserDTO userDTO = userService.loadUserDataByEmail(authenticatedUser.getEmail());
            imageService.getProfileImage(authenticatedUser.getId()).ifPresent(photo ->{
                userDTO.setProfilePhotoUrl(photo.getPhotoUrl());
            });

            loginResponse.setUserDTO(userDTO);

            return ResponseEntity.ok(loginResponse);
>>>>>>> fda96bb (Add Dockerized backend and MySQL setup)
        } catch (Exception e) {
            LOGGER.warn("Login failed for email {}", loginUserDto.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad username or password");
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> fda96bb (Add Dockerized backend and MySQL setup)
