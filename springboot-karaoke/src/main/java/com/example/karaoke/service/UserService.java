package com.example.karaoke.service;

import java.util.Optional;

import com.example.karaoke.model.dto.LoginDTO;
import com.example.karaoke.model.dto.UserDTO;

/*
 * 1.根據帳號查詢使用者
 * 2.登入 login
 * 3.新增/儲存使用者
 */
public interface UserService {
	Optional<UserDTO> findByUsername(String username);
	Optional<UserDTO> login(LoginDTO loginDTO);
	Optional<UserDTO> saveUser(UserDTO userDTO);
}
