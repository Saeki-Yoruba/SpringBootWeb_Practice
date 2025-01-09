package com.example.karaoke.model.dto;

import lombok.Data;

@Data
public class LoginDTO {
	private String username;
	private String password;
	private Boolean isLoggedIn;
}
