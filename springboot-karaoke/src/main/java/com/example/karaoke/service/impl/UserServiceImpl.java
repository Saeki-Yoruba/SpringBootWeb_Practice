package com.example.karaoke.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.karaoke.model.dto.LoginDTO;
import com.example.karaoke.model.dto.UserDTO;
import com.example.karaoke.model.entity.User;
import com.example.karaoke.repository.UserRepository;
import com.example.karaoke.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Optional<UserDTO> findByUsername(String username) {
		Optional<User> optUser = userRepository.findByUsername(username);
		if(optUser.isEmpty()) {
			return Optional.empty();
		}
		// 利用 modelMapper 將 entity 轉 DTO
		UserDTO userDTO = modelMapper.map(optUser.get(), UserDTO.class);
		return Optional.of(userDTO);
	}
	
	/*
	@Override
	public Optional<UserDTO> login(LoginDTO loginDTO) {
		// 1. 透過 username 找到 user
		Optional<User> optUser = userRepository.findByUsername(loginDTO.getUsername());
		if(optUser.isEmpty()) {
			return Optional.empty();
		}
		// 2. 判斷密碼
		User user = optUser.get();
		if(user.getPassword().equals(loginDTO.getPassword())) {
			// 3. entity 轉 DTO
			UserDTO userDTO = modelMapper.map(user, UserDTO.class);
			return Optional.of(userDTO);
		}
		return Optional.empty();
	}
	*/
	@Override
	public Optional<UserDTO> login(LoginDTO loginDTO) {
		// 1. 透過 username 找到 user
		Optional<User> optUser = userRepository.findByUsername(loginDTO.getUsername());
		// 2. 判斷密碼
		if(optUser.isPresent() && optUser.get().getPassword().equals(loginDTO.getPassword())) {
			return Optional.of(modelMapper.map(optUser.get(), UserDTO.class));
		}
		return Optional.empty();
	
	}

	@Override
    public Optional<UserDTO> saveUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("使用者名稱已被註冊");
        }
        User user = userRepository.save(modelMapper.map(userDTO, User.class));
        return Optional.of(modelMapper.map(user, UserDTO.class));
    }
}
