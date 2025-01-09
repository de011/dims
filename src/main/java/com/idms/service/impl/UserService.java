package com.idms.service.impl;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    public boolean validateUser (String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return "admin".equals(username) && "DriveSoft@@!".equals(password);
    }
}
