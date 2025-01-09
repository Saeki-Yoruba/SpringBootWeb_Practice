package com.example.karaoke.exception;

public class RoomNotFoundException extends RuntimeException {
	public RoomNotFoundException() {
		super("查無房間");
	}
	
	public RoomNotFoundException(String message) {
		super(message);
	}
}
