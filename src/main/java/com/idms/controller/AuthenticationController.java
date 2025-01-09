package com.idms.controller;

import com.idms.dto.AuthResponse;
import com.idms.service.impl.UserService;
import com.idms.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Value("${auth.username}")
    private String username;

    @Value("${auth.password}")
    private String password;

    public AuthenticationController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate() {
        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Username or password cannot be null"));
        }

        if (userService.validateUser (username, password)) {
            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid credentials"));
    }
}