package com.example.karaoke.model.dto;

import lombok.Data;

@Data
public class RoomDTO {
	
	private Long id;
	private String roomNumber;
	private int capacity;
	private String machineType;
	private Double hourlyPrice;
	
}
