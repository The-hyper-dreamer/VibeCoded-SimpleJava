package com.iesce.maintenance.ui;

import com.iesce.maintenance.model.User;
import com.iesce.maintenance.service.AuthenticationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private final AuthenticationService authService = new AuthenticationService();
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private JCheckBox showPasswordCheck;

    // Color palette
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color PRIMARY_DARK = new Color(31, 97, 141);
    private static final Color BG_COLOR = new Color(245, 247, 250);
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Color LABEL_COLOR = new Color(80, 80, 80);
    private static final Color ERROR_COLOR = new Color(220, 53, 69);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);

    public LoginFrame() {
        setTitle("Classroom Maintenance Request System - Login");
        setSize(500, 420);
        setMinimumSize(new Dimension(450, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        initUI();
    }

    private void initUI() {
        getContentPane().setBackground(BG_COLOR);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header panel with title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BG_COLOR);
        JLabel titleLabel = new JLabel("Classroom Maintenance");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        JLabel subtitleLabel = new JLabel("Request System - Login");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(LABEL_COLOR);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        headerPanel.add(subtitleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PANEL_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(LABEL_COLOR);
        formPanel.add(userLabel, gbc);

        gbc.gridy = 1;
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(LABEL_COLOR);
        formPanel.add(passLabel, gbc);

        gbc.gridy = 3;
        JPanel passPanel = new JPanel(new BorderLayout(5, 0));
        passPanel.setBackground(PANEL_COLOR);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        showPasswordCheck = new JCheckBox("Show");
        showPasswordCheck.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        showPasswordCheck.setBackground(PANEL_COLOR);
        showPasswordCheck.setForeground(LABEL_COLOR);
        showPasswordCheck.setFocusPainted(false);
        showPasswordCheck.addActionListener(e -> {
            if (showPasswordCheck.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
        passPanel.add(passwordField, BorderLayout.CENTER);
        passPanel.add(showPasswordCheck, BorderLayout.EAST);
        formPanel.add(passPanel, gbc);

        // Login button
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(PRIMARY_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(PRIMARY_COLOR);
            }
        });
        loginButton.addActionListener(e -> handleLogin());
        formPanel.add(loginButton, gbc);

        // Status label
        gbc.gridy = 5;
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(ERROR_COLOR);
        formPanel.add(statusLabel, gbc);

        // Demo accounts
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel demoLabel = new JLabel("<html><div style='text-align:center;'>Demo accounts:<br/><b>Faculty:</b> faculty1 / faculty123<br/><b>HOD:</b> hod1 / hod123</div></html>");
        demoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        demoLabel.setForeground(new Color(120, 120, 120));
        formPanel.add(demoLabel, gbc);

        passwordField.addActionListener(e -> handleLogin());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        statusLabel.setText("");

        if (username.isEmpty()) {
            statusLabel.setForeground(ERROR_COLOR);
            statusLabel.setText("Please enter your username.");
            usernameField.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            statusLabel.setForeground(ERROR_COLOR);
            statusLabel.setText("Please enter your password.");
            passwordField.requestFocus();
            return;
        }

        try {
            User user = authService.authenticate(username, password);
            statusLabel.setForeground(SUCCESS_COLOR);
            statusLabel.setText("Login successful! Opening dashboard...");

            SwingUtilities.invokeLater(() -> {
                dispose();
                if (user.isFaculty()) {
                    new FacultyDashboard(user).setVisible(true);
                } else if (user.isHOD()) {
                    new HODDashboard(user).setVisible(true);
                }
            });
        } catch (Exception ex) {
            statusLabel.setForeground(ERROR_COLOR);
            statusLabel.setText(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel
            }
            new LoginFrame().setVisible(true);
        });
    }
}
