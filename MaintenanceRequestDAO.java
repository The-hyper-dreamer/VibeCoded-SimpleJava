package com.iesce.maintenance.dao;

import com.iesce.maintenance.model.MaintenanceRequest;
import com.iesce.maintenance.model.User;
import com.iesce.maintenance.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceRequestDAO {

    public int create(MaintenanceRequest request) {
        String sql = "INSERT INTO maintenance_requests (faculty_id, location, category, description, status, created_at, updated_at) VALUES (?, ?, ?, ?, 'PENDING', ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, request.getFacultyId());
            stmt.setString(2, request.getLocation());
            stmt.setString(3, request.getCategory());
            stmt.setString(4, request.getDescription());
            stmt.setTimestamp(5, Timestamp.valueOf(request.getCreatedAt()));
            stmt.setTimestamp(6, Timestamp.valueOf(request.getUpdatedAt()));

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating request failed, no rows affected.");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                } else {
                    throw new SQLException("Creating request failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error creating request: " + e.getMessage(), e);
        }
    }

    public List<MaintenanceRequest> findByFaculty(int facultyId) {
        String sql = "SELECT mr.id, mr.faculty_id, u.username AS faculty_name, mr.location, mr.category, " +
                     "mr.description, mr.status, mr.hod_remarks, mr.created_at, mr.updated_at, mr.completed_at " +
                     "FROM maintenance_requests mr JOIN users u ON mr.faculty_id = u.id " +
                     "WHERE mr.faculty_id = ? ORDER BY mr.created_at DESC";
        return query(sql, facultyId);
    }

    public List<MaintenanceRequest> findAll() {
        String sql = "SELECT mr.id, mr.faculty_id, u.username AS faculty_name, mr.location, mr.category, " +
                     "mr.description, mr.status, mr.hod_remarks, mr.created_at, mr.updated_at, mr.completed_at " +
                     "FROM maintenance_requests mr JOIN users u ON mr.faculty_id = u.id " +
                     "ORDER BY mr.created_at DESC";
        return query(sql, -1);
    }

    public void updateStatusAndRemarks(int requestId, String status, String remarks) {
        String sql;
        if ("COMPLETED".equalsIgnoreCase(status)) {
            sql = "UPDATE maintenance_requests SET status = ?, hod_remarks = ?, updated_at = NOW(), completed_at = NOW() WHERE id = ?";
        } else {
            sql = "UPDATE maintenance_requests SET status = ?, hod_remarks = ?, updated_at = NOW() WHERE id = ?";
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, remarks);
            stmt.setInt(3, requestId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error updating request: " + e.getMessage(), e);
        }
    }

    public void updateRemarks(int requestId, String remarks) {
        String sql = "UPDATE maintenance_requests SET hod_remarks = ?, updated_at = NOW() WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, remarks);
            stmt.setInt(2, requestId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error updating remarks: " + e.getMessage(), e);
        }
    }

    private List<MaintenanceRequest> query(String sql, int facultyId) {
        List<MaintenanceRequest> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (facultyId >= 0) {
                stmt.setInt(1, facultyId);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MaintenanceRequest req = new MaintenanceRequest();
                    req.setId(rs.getInt("id"));
                    req.setFacultyId(rs.getInt("faculty_id"));
                    req.setFacultyName(rs.getString("faculty_name"));
                    req.setLocation(rs.getString("location"));
                    req.setCategory(rs.getString("category"));
                    req.setDescription(rs.getString("description"));
                    req.setStatus(rs.getString("status"));
                    req.setHodRemarks(rs.getString("hod_remarks"));
                    req.setCreatedAt(toLocalDateTime(rs.getTimestamp("created_at")));
                    req.setUpdatedAt(toLocalDateTime(rs.getTimestamp("updated_at")));
                    Timestamp completed = rs.getTimestamp("completed_at");
                    req.setCompletedAt(completed != null ? toLocalDateTime(completed) : null);
                    list.add(req);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error querying requests: " + e.getMessage(), e);
        }
        return list;
    }

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts != null ? ts.toLocalDateTime() : null;
    }
}
