package com.example.unlight.exception;

import lombok.experimental.SuperBuilder;

public class CharacterNotFoundException extends RuntimeException{
	public CharacterNotFoundException() {
		super("查無角色");
	}
	
	public CharacterNotFoundException(String message) {
		super(message);
	}
}
