package com.iesce.maintenance.service;

import com.iesce.maintenance.dao.UserDAO;
import com.iesce.maintenance.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AuthenticationService {

    private final UserDAO userDAO = new UserDAO();

    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        User user = userDAO.findByUsername(username.trim());
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        if (!user.isActive()) {
            throw new IllegalArgumentException("Account is deactivated. Contact administrator.");
        }

        String passwordHash = hashPassword(password);
        if (!userDAO.verifyPassword(username.trim(), passwordHash)) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        return user;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available: " + e.getMessage(), e);
        }
    }

    public String generateHash(String password) {
        return hashPassword(password);
    }

    public boolean isFaculty(User user) {
        return user != null && user.isFaculty();
    }

    public boolean isHOD(User user) {
        return user != null && user.isHOD();
    }
}
