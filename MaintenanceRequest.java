package com.iesce.maintenance.model;

import java.time.LocalDateTime;

public class MaintenanceRequest {

    private int id;
    private int facultyId;
    private String facultyName;
    private String location;
    private String category;
    private String description;
    private String status;
    private String hodRemarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;

    public MaintenanceRequest() {
    }

    public MaintenanceRequest(int id, int facultyId, String facultyName, String location,
                              String category, String description, String status,
                              String hodRemarks, LocalDateTime createdAt,
                              LocalDateTime updatedAt, LocalDateTime completedAt) {
        this.id = id;
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.location = location;
        this.category = category;
        this.description = description;
        this.status = status;
        this.hodRemarks = hodRemarks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHodRemarks() {
        return hodRemarks;
    }

    public void setHodRemarks(String hodRemarks) {
        this.hodRemarks = hodRemarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public String toString() {
        return "Request #" + id + " - " + location + " (" + category + ") - " + status;
    }
}
