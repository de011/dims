package com.idms.controller;

import com.idms.dto.AuthRequest;
import com.idms.dto.AuthResponse;
import com.idms.utility.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private final JwtUtil jwtUtil;

    public AuthenticationController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

/*    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        if ("admin".equals(authRequest.getUsername()) && "DriveSoft@@!".equals(authRequest.getPassword())) {
            String token = jwtUtil.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid credentials"));
    }*/

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate() {
        String username = "admin";
        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}