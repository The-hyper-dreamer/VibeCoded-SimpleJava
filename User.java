package com.iesce.maintenance.model;

import java.time.LocalDateTime;

public class User {

    private int id;
    private String username;
    private String role;
    private boolean active;

    public User() {
    }

    public User(int id, String username, String role, boolean active) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFaculty() {
        return "FACULTY".equalsIgnoreCase(role);
    }

    public boolean isHOD() {
        return "HOD".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}
