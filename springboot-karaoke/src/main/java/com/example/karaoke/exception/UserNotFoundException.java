package com.example.karaoke.exception;

// 查無用戶
public class UserNotFoundException extends RuntimeException{
	
	public UserNotFoundException() {
		super("查無用戶");
	}
	
	public UserNotFoundException(String message) {
		super(message);
	}

}
