package com.example.unlight.controller;

import com.example.unlight.model.Character;
import com.example.unlight.response.ApiResponse;
import com.example.unlight.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    // 查詢單個角色 (對所有用戶開放)
    @GetMapping("/{id}")
    public ApiResponse<Character> getCharacterById(@PathVariable Long id) {
        Character character = characterService.getCharacterById(id);
        return ApiResponse.success("查詢成功", character);
    }

    // 查詢所有角色 (對所有用戶開放)
    @GetMapping
    public ApiResponse<List<Character>> getAllCharacters() {
        List<Character> characters = characterService.getAllCharacters();
        return ApiResponse.success("查詢成功", characters);
    }

    // 新增角色 (僅限管理員)
    @PostMapping
    public ApiResponse<Void> addCharacter(@RequestBody Character character, @RequestHeader(value = "Role", defaultValue = "USER") String role) {
        checkAdmin(role);
        characterService.addCharacter(character);
        return ApiResponse.success("新增成功", null);
    }

    // 更新角色 (僅限管理員)
    @PutMapping("/{id}")
    public ApiResponse<Void> updateCharacter(@PathVariable Long id, @RequestBody Character character, @RequestHeader(value = "Role", defaultValue = "USER") String role) {
        checkAdmin(role);
        characterService.updateCharacter(id, character);
        return ApiResponse.success("更新成功", null);
    }

    // 刪除角色 (僅限管理員)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCharacter(@PathVariable Long id, @RequestHeader(value = "Role", defaultValue = "USER") String role) {
        checkAdmin(role);
        characterService.deleteCharacter(id);
        return ApiResponse.success("刪除成功", null);
    }

    // 檢查是否為管理員
    private void checkAdmin(String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new SecurityException("無權限進行此操作");
        }
    }
}
