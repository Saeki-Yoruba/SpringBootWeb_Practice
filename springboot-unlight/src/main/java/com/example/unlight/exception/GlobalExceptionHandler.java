package com.example.unlight.exception;

import com.example.unlight.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CharacterNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleCharacterNotFoundException(CharacterNotFoundException ex) {
        return ResponseEntity.status(404).body(ApiResponse.error(404, ex.getMessage()));
    }

    @ExceptionHandler(StoryNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleStoryNotFoundException(StoryNotFoundException ex) {
        return ResponseEntity.status(404).body(ApiResponse.error(404, ex.getMessage()));
    }

    @ExceptionHandler(DuplicateStoryException.class)
    public ResponseEntity<ApiResponse<String>> handleDuplicateStoryException(DuplicateStoryException ex) {
        return ResponseEntity.status(409).body(ApiResponse.error(409, ex.getMessage()));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiResponse<String>> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(403).body(ApiResponse.error(403, ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(401).body(ApiResponse.error(401, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(500).body(ApiResponse.error(500, "伺服器錯誤: " + ex.getMessage()));
    }
}
