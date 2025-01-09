package com.example.unlight.dao;

import com.example.unlight.model.Story;
import com.example.unlight.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StoryDAO {

    @Autowired
    private DatabaseConnection db;

    // 查詢所有故事
    public List<Story> findAll() {
        String sql = "SELECT * FROM stories";
        List<Story> stories = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                stories.add(mapResultSetToStory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all stories", e);
        }
        return stories;
    }

    // 根據 ID 查詢故事
    public Optional<Story> findById(Long id) {
        String sql = "SELECT * FROM stories WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Story story = mapResultSetToStory(rs);
                    story.setImages(findImagesByStoryId(id)); // 加載圖片
                    return Optional.of(story);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching story by ID: " + id, e);
        }
        return Optional.empty();
    }

    // 根據角色 ID 查詢故事
    public List<Story> findByCharacterId(Long characterId) {
        String sql = "SELECT * FROM stories WHERE character_id = ?";
        List<Story> stories = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, characterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Story story = mapResultSetToStory(rs);
                    story.setImages(findImagesByStoryId(story.getId())); // 加載圖片
                    stories.add(story);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching stories by character ID: " + characterId, e);
        }
        return stories;
    }

    // 查詢與故事相關的所有圖片
    public List<String> findImagesByStoryId(Long storyId) {
        String sql = "SELECT image_url FROM story_images WHERE story_id = ?";
        List<String> images = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    images.add(rs.getString("image_url"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching images for story ID: " + storyId, e);
        }
        return images;
    }

    // 新增故事
    public void save(Story story, List<String> images) {
        String sql = "INSERT INTO stories (title, content, chapter, character_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, story.getTitle());
            ps.setString(2, story.getContent());
            ps.setInt(3, story.getChapter());
            ps.setLong(4, story.getCharacterId());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    story.setId(generatedKeys.getLong(1));
                }
            }

            if (images != null && !images.isEmpty()) {
                saveStoryImages(story.getId(), images);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving story", e);
        }
    }

    // 保存故事的圖片
    public void saveStoryImages(Long storyId, List<String> images) {
        String sql = "INSERT INTO story_images (story_id, image_url) VALUES (?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String imageUrl : images) {
                ps.setLong(1, storyId);
                ps.setString(2, imageUrl);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving story images for story ID: " + storyId, e);
        }
    }

    // 更新故事
    public void update(Story story, List<String> images) {
        String sql = "UPDATE stories SET title = ?, content = ?, chapter = ?, character_id = ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, story.getTitle());
            ps.setString(2, story.getContent());
            ps.setInt(3, story.getChapter());
            ps.setLong(4, story.getCharacterId());
            ps.setLong(5, story.getId());
            ps.executeUpdate();

            if (images != null && !images.isEmpty()) {
                deleteStoryImages(story.getId());
                saveStoryImages(story.getId(), images);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating story", e);
        }
    }

    // 刪除故事
    public void delete(Long id) {
        String sql = "DELETE FROM stories WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            deleteStoryImages(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting story with ID: " + id, e);
        }
    }

    // 刪除與故事相關的所有圖片
    public void deleteStoryImages(Long storyId) {
        String sql = "DELETE FROM story_images WHERE story_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storyId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting images for story ID: " + storyId, e);
        }
    }

    // 將 ResultSet 映射到 Story 對象
    private Story mapResultSetToStory(ResultSet rs) {
        try {
            return new Story(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getInt("chapter"),
                rs.getLong("character_id"),
                null // 圖片在外部設置
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping ResultSet to Story", e);
        }
    }
}
