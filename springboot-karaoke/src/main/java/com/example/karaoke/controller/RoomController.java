package com.example.karaoke.controller;

import com.example.karaoke.model.dto.RoomDTO;
import com.example.karaoke.response.ApiResponse;
import com.example.karaoke.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class RoomController {

    @Autowired
    private RoomService roomService;

    /**
     * 查詢所有房間
     * @return 所有房間的列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomDTO>>> getAllRooms() {
      return ResponseEntity.ok(ApiResponse.success("查詢成功", roomService.getAllRooms()));
    }

    /**
     * 根據房間 ID 查詢房間
     * @param roomId 房間 ID
     * @return 指定房間的詳細資訊
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomDTO>> getRoomById(@PathVariable Long id) {
        Optional<RoomDTO> optRoomDTO = roomService.getRoomById(id);
        if(optRoomDTO.isEmpty()) {
        	return ResponseEntity.status(404).body(ApiResponse.error(404, "查無房間"));
        }
        return ResponseEntity.ok(ApiResponse.success("查詢成功", optRoomDTO.get()));
    }

    /**
     * 新增房間
     */
    @PostMapping
    public ResponseEntity<ApiResponse<RoomDTO>> addRoom(@RequestBody RoomDTO roomDTO) {
        Optional<RoomDTO> optRoomDTO = roomService.saveRoom(roomDTO);
        if(optRoomDTO.isEmpty()) {
        	return ResponseEntity.status(404).body(ApiResponse.error(404, "新增失敗"));
        }
        return ResponseEntity.ok (ApiResponse.success("新增成功", optRoomDTO.get()));
    }
}
