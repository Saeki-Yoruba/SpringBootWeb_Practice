package com.example.karaoke.model.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BookingDTO {
    private Long id;
    private Long userId;
    private Long roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
