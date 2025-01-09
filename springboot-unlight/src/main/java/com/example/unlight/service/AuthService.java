package com.example.unlight.service;

import com.example.unlight.dao.UserDAO;
import com.example.unlight.exception.AuthenticationException;
import com.example.unlight.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDAO userDAO;

    // 驗證用戶名和密碼
    public User authenticate(String username, String password) {
        return userDAO.findByUsername(username)
                .filter(user -> user.getPassword().equals(password)) // 驗證密碼
                .orElseThrow(() -> new AuthenticationException("用戶名或密碼錯誤"));
    }
}
