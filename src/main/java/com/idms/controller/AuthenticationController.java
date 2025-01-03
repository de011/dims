package com.idms.controller;

import com.idms.utility.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private final JwtUtil jwtUtil;

    public AuthenticationController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Basic ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Missing or invalid Authorization header");
        }
        String credentials = new String(Base64.getDecoder().decode(authorization.substring(6)));
        String[] parts = credentials.split(":");
        String username = parts[0];
        String password = parts[1];

        if ("admin".equals(username) && "DriveSoft@@!".equals(password)) {
            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
