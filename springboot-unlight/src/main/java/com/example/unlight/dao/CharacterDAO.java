package com.example.unlight.dao;

import com.example.unlight.model.Character;
import com.example.unlight.util.DatabaseConnection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CharacterDAO {

    @Autowired
    private DatabaseConnection db;

    @Autowired
    private ObjectMapper objectMapper;

    // 查詢所有角色
    public List<Character> findAll() {
        String sql = "SELECT * FROM characters";
        List<Character> characters = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                characters.add(mapResultSetToCharacter(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all characters", e);
        }
        return characters;
    }

    // 根據 ID 查詢角色
    public Optional<Character> findById(Long id) {
        String sql = "SELECT * FROM characters WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCharacter(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching character by ID: " + id, e);
        }
        return Optional.empty();
    }

    // 新增角色
    public void save(Character character) {
        String sql = "INSERT INTO characters (name, description, main_image, images) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, character.getName());
            ps.setString(2, character.getDescription());
            ps.setString(3, character.getMainImage()); // 主圖片
            ps.setString(4, convertListToJson(character.getImages()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving character", e);
        }
    }

    // 更新角色
    public void update(Character character) {
        String sql = "UPDATE characters SET name = ?, description = ?, main_image = ?, images = ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, character.getName());
            ps.setString(2, character.getDescription());
            ps.setString(3, character.getMainImage()); // 主圖片
            ps.setString(4, convertListToJson(character.getImages()));
            ps.setLong(5, character.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating character", e);
        }
    }

    // 刪除角色
    public void delete(Long id) {
        String sql = "DELETE FROM characters WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting character", e);
        }
    }

    // 將 ResultSet 映射到 Character 對象
    private Character mapResultSetToCharacter(ResultSet rs) {
        try {
            return new Character(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("main_image"), // 主圖片
                convertJsonToList(rs.getString("images"))
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping ResultSet to Character", e);
        }
    }

    // 將 List<String> 轉換為 JSON
    private String convertListToJson(List<String> images) {
        try {
            return objectMapper.writeValueAsString(images != null ? images : new ArrayList<>());
        } catch (Exception e) {
            throw new RuntimeException("Error converting list to JSON", e);
        }
    }

    // 將 JSON 轉換為 List<String>
    private List<String> convertJsonToList(String json) {
        try {
            return objectMapper.readValue(json != null ? json : "[]", new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to list", e);
        }
    }
}
