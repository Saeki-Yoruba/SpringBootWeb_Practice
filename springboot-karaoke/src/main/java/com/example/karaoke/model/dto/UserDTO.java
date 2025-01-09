package com.example.karaoke.model.dto;

import lombok.Data;

@Data
public class UserDTO {

	private Long id;
	private String username; // 確保對應數據庫的 user_name 字段
    private String email;
    private String phone;
    private String password;
	
}
