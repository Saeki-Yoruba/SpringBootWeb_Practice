package com.example.karaoke.exception;

// 查無訂位資料
public class BookingNotFoundException extends Exception{

	public BookingNotFoundException() {
		super("查無訂位資料");
	}
	
	public BookingNotFoundException(String message) {
		super(message);
	}
	
}

