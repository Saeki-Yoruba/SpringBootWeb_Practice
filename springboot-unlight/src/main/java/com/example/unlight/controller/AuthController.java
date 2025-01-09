package com.example.unlight.controller;

import com.example.unlight.model.User;
import com.example.unlight.response.ApiResponse;
import com.example.unlight.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 用戶登入
    @PostMapping("/login")
    public ApiResponse<User> login(@RequestBody User user) {
        User authenticatedUser = authService.authenticate(user.getUsername(), user.getPassword());
        if (authenticatedUser == null) {
            throw new IllegalArgumentException("用戶名或密碼錯誤");
        }
        return ApiResponse.success("登入成功", authenticatedUser);
    }
}
