package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.DTO.LoginDTO;
import com.strongBeton.strongBeton.DTO.RegisterUserDTO;
import com.strongBeton.strongBeton.DTO.UserDTO;
import com.strongBeton.strongBeton.entity.User;
import com.strongBeton.strongBeton.service.AuthService;
import com.strongBeton.strongBeton.service.ImageService;
import com.strongBeton.strongBeton.service.JwtService;
import com.strongBeton.strongBeton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/auth")
@RestController
public class AuthRestController {

    private final JwtService jwtService;

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
    public ResponseEntity<User> register (@RequestBody RegisterUserDTO registerUserDTO){
        System.out.println("Here i start work!");
        System.out.println(registerUserDTO);
        User registeredUser = authService.signup(registerUserDTO);
        System.out.println("Here i finish register!");
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDTO loginUserDto) {
        try {
            User authenticatedUser = authService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());
            UserDTO userDTO = userService.loadUserDataByUsername(authenticatedUser.getUsername());
            userDTO.setProfilePhotoUrl(imageService.getProfileImage(userDTO.getId()).getPhotoUrl());
            loginResponse.setUserDTO(userService.loadUserDataByUsername(authenticatedUser.getUsername()));
            return ResponseEntity.ok(loginResponse);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
