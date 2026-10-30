package com.iesce.maintenance.dao;

import com.iesce.maintenance.model.User;
import com.iesce.maintenance.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User findByUsername(String username) {
        String sql = "SELECT id, username, role, active FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setRole(rs.getString("role"));
                    user.setActive(rs.getBoolean("active"));
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error finding user: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean verifyPassword(String username, String passwordHash) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password_hash = ? AND active = TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error verifying password: " + e.getMessage(), e);
        }
        return false;
    }
}
