package com.example.karaoke.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.karaoke.model.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{

}
