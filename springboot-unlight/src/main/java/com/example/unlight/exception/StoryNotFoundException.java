package com.example.unlight.exception;

public class StoryNotFoundException extends RuntimeException{
	public StoryNotFoundException() {
		super("查無故事");
	}
	public StoryNotFoundException(String message) {
		super(message);
	}
}
