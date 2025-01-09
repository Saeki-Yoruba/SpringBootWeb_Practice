package com.example.unlight.exception;

public class DuplicateStoryException extends RuntimeException {
    public DuplicateStoryException(String message) {
        super(message);
    }
}
