package com.example.unlight.service;

import com.example.unlight.dao.CharacterDAO;
import com.example.unlight.exception.CharacterNotFoundException;
import com.example.unlight.model.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterService {

    @Autowired
    private CharacterDAO characterDAO;

    // 查詢所有角色
    public List<Character> getAllCharacters() {
        return characterDAO.findAll();
    }

    // 根據 ID 查詢角色
    public Character getCharacterById(Long id) {
        return characterDAO.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException("角色不存在，ID: " + id));
    }

    // 新增角色
    public void addCharacter(Character character) {
        validateCharacter(character);
        characterDAO.save(character);
    }

    // 更新角色
    public void updateCharacter(Long id, Character character) {
        Character existingCharacter = ensureCharacterExists(id);
        validateCharacter(character);
        character.setId(existingCharacter.getId());
        characterDAO.update(character);
    }

    // 刪除角色
    public void deleteCharacter(Long id) {
        ensureCharacterExists(id);
        characterDAO.delete(id);
    }

    // 檢查角色是否存在，返回角色
    private Character ensureCharacterExists(Long id) {
        return characterDAO.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException("角色不存在，ID: " + id));
    }

    // 驗證角色信息
    private void validateCharacter(Character character) {
        if (character.getName() == null || character.getName().isEmpty()) {
            throw new IllegalArgumentException("角色名稱不能為空");
        }
        if (character.getMainImage() == null || character.getMainImage().isEmpty()) {
            throw new IllegalArgumentException("角色必須提供主圖片");
        }
    }
}
