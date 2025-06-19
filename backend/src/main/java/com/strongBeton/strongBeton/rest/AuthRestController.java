package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.DTO.LoginDTO;
import com.strongBeton.strongBeton.DTO.RegisterUserDTO;
import com.strongBeton.strongBeton.entity.User;
import com.strongBeton.strongBeton.service.AuthService;
import com.strongBeton.strongBeton.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/auth")
@RestController
public class AuthRestController {

    private final JwtService jwtService;

    private final AuthService authService;

    @Autowired
    public AuthRestController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register (@RequestBody RegisterUserDTO registerUserDTO){
        User registeredUser = authService.signup(registerUserDTO);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDTO loginUserDto) {
        System.out.println("authenticate works fine before authService!");
        User authenticatedUser = authService.authenticate(loginUserDto);
        System.out.println("authenticate works fine before jwtToken!");
        String jwtToken = jwtService.generateToken(authenticatedUser);
        System.out.println("After generating Token");
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        System.out.println("PostMethod Login works fine!");
        return ResponseEntity.ok(loginResponse);
    }
}
