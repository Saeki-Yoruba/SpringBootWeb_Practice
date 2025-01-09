package com.example.karaoke.service;

import java.util.List;
import java.util.Optional;

import com.example.karaoke.exception.BookingNotFoundException;
import com.example.karaoke.exception.RoomNotFoundException;
import com.example.karaoke.model.dto.RoomDTO;

/*
 * 1.取得所有房間
 * 2.取得指定房間
 * 3.新增/儲存房間
 */
public interface RoomService {
	List<RoomDTO> getAllRooms() throws RoomNotFoundException;
	Optional<RoomDTO> getRoomById(Long id) throws RoomNotFoundException;
	Optional<RoomDTO> saveRoom(RoomDTO roomDTO) throws RoomNotFoundException;
}
