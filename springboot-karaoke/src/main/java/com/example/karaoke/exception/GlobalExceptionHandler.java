package com.example.karaoke.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.karaoke.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleBookingNotFoundException(BookingNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error(404, ex.getMessage()));
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleRoomNotFoundException(RoomNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error(404, ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error(404, ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<String>> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(401) // 未經授權
                .body(ApiResponse.error(401, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500)
                .body(ApiResponse.error(500, "伺服器內部錯誤"));
    }
}

