package com.example.karaoke.exception;

// 登入不成功
public class UnauthorizedException extends Exception{
	public UnauthorizedException(String message) {
		super(message);
	}
}
