package com.example.unlight.dao;

import com.example.unlight.model.User;
import com.example.unlight.util.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class UserDAO {

    @Autowired
    private DatabaseConnection db;

    // 根據用戶名查詢用戶
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM unlightUsers WHERE username = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by username: " + username, e);
        }
        return Optional.empty();
    }

    // 將 ResultSet 映射到 User 對象
    private User mapResultSetToUser(ResultSet rs) {
        try {
            return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping ResultSet to User", e);
        }
    }
}
