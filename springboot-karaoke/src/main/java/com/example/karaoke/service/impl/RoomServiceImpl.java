package com.example.karaoke.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.karaoke.model.dto.RoomDTO;
import com.example.karaoke.model.entity.Room;
import com.example.karaoke.repository.RoomRepository;
import com.example.karaoke.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService{

	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<RoomDTO> getAllRooms(){
		return roomRepository.findAll()
							 .stream()
							 .map(room -> modelMapper.map(room, RoomDTO.class))
							 .toList();
	}
	
	@Override
	public Optional<RoomDTO> getRoomById(Long id){
		Optional<Room> optRoom = roomRepository.findById(id);
		if(optRoom.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(modelMapper.map(optRoom.get(), RoomDTO.class));
	}
	
	@Override
	public Optional<RoomDTO> saveRoom(RoomDTO roomDTO){
		Room room = modelMapper.map(roomDTO, Room.class);
		roomRepository.save(room);
		return Optional.of(modelMapper.map(room, RoomDTO.class));
	}
}
