package com.iesce.maintenance.ui;

import com.iesce.maintenance.model.MaintenanceRequest;
import com.iesce.maintenance.model.User;
import com.iesce.maintenance.service.RequestService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FacultyDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private final User faculty;
    private final RequestService requestService = new RequestService();
    private DefaultTableModel pendingModel;
    private DefaultTableModel approvedModel;
    private DefaultTableModel rejectedModel;
    private DefaultTableModel completedModel;

    // Color palette
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color PRIMARY_DARK = new Color(31, 97, 141);
    private static final Color BG_COLOR = new Color(245, 247, 250);
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Color LABEL_COLOR = new Color(80, 80, 80);
    private static final Color SUCCESS_COLOR = new Color(46, 125, 50);
    private static final Color WARNING_COLOR = new Color(230, 126, 34);
    private static final Color DANGER_COLOR = new Color(183, 28, 28);
    private static final Color INFO_COLOR = new Color(25, 118, 210);
    private static final Color STATUS_PENDING = new Color(241, 196, 15);
    private static final Color STATUS_APPROVED = new Color(46, 204, 113);
    private static final Color STATUS_REJECTED = new Color(192, 57, 43);
    private static final Color STATUS_COMPLETED = new Color(52, 73, 94);
    private static final Color TABLE_HEADER_COLOR = new Color(41, 128, 185);

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");

    public FacultyDashboard(User faculty) {
        this.faculty = faculty;
        setTitle("Faculty Dashboard - " + faculty.getUsername());
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG_COLOR);

        initUI();
        refreshData();

        setVisible(true);
    }

    private void initUI() {
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Faculty Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("Logged in as: " + faculty.getUsername() + "  |  Role: FACULTY");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(200, 220, 240));

        JLabel roleBadge = new JLabel("FACULTY");
        roleBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        roleBadge.setForeground(Color.WHITE);
        roleBadge.setBackground(new Color(52, 152, 219));
        roleBadge.setOpaque(true);
        roleBadge.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        roleBadge.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel leftHeader = new JPanel(new BorderLayout());
        leftHeader.setBackground(PRIMARY_COLOR);
        leftHeader.add(titleLabel, BorderLayout.NORTH);
        leftHeader.add(userLabel, BorderLayout.SOUTH);

        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(roleBadge, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane for request sections
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(BG_COLOR);

        pendingModel = new DefaultTableModel(new String[]{"ID", "Location", "Category", "Submitted", "Status"}, 0);
        approvedModel = new DefaultTableModel(new String[]{"ID", "Location", "Category", "Submitted", "Status"}, 0);
        rejectedModel = new DefaultTableModel(new String[]{"ID", "Location", "Category", "Submitted", "Status"}, 0);
        completedModel = new DefaultTableModel(new String[]{"ID", "Location", "Category", "Submitted", "Status"}, 0);

        tabbedPane.addTab("Pending Requests", createTablePanel(pendingModel, STATUS_PENDING));
        tabbedPane.addTab("Approved (Not Completed)", createTablePanel(approvedModel, STATUS_APPROVED));
        tabbedPane.addTab("Rejected (Latest 2)", createTablePanel(rejectedModel, STATUS_REJECTED));
        tabbedPane.addTab("Completed (Latest 2)", createTablePanel(completedModel, STATUS_COMPLETED));

        add(tabbedPane, BorderLayout.CENTER);

        // Bottom action panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(PANEL_COLOR);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JButton newRequestBtn = new JButton("+ Submit New Request");
        newRequestBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newRequestBtn.setBackground(SUCCESS_COLOR);
        newRequestBtn.setForeground(Color.WHITE);
        newRequestBtn.setFocusPainted(false);
        newRequestBtn.setBorderPainted(false);
        newRequestBtn.setPreferredSize(new Dimension(220, 40));
        newRequestBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        newRequestBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                newRequestBtn.setBackground(new Color(39, 104, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                newRequestBtn.setBackground(SUCCESS_COLOR);
            }
        });
        newRequestBtn.addActionListener(e -> showNewRequestDialog());
        bottomPanel.add(newRequestBtn, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setBackground(new Color(149, 165, 166));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setPreferredSize(new Dimension(100, 40));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        bottomPanel.add(logoutBtn, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTablePanel(DefaultTableModel model, Color statusColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(new Color(220, 220, 220));
        table.setIntercellSpacing(new Dimension(1, 1));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(TABLE_HEADER_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(31, 97, 141)));
        table.getTableHeader().setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                } else {
                    c.setBackground(new Color(200, 225, 245));
                }
                c.setForeground(TEXT_COLOR);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));

                if (column == 4 && value != null) {
                    String status = value.toString().toUpperCase();
                    setForeground(Color.WHITE);
                    setOpaque(true);
                    switch (status) {
                        case "PENDING":
                            setBackground(STATUS_PENDING);
                            break;
                        case "APPROVED":
                            setBackground(STATUS_APPROVED);
                            break;
                        case "REJECTED":
                            setBackground(STATUS_REJECTED);
                            break;
                        case "COMPLETED":
                            setBackground(STATUS_COMPLETED);
                            break;
                        default:
                            setBackground(LABEL_COLOR);
                    }
                } else {
                    setOpaque(false);
                }
                return c;
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                int id = (int) model.getValueAt(row, 0);
                showDetails(id);
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void refreshData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    List<MaintenanceRequest> all = requestService.getFacultyRequests(faculty);
                    pendingModel.setRowCount(0);
                    approvedModel.setRowCount(0);
                    rejectedModel.setRowCount(0);
                    completedModel.setRowCount(0);

                    int rejectedCount = 0;
                    int completedCount = 0;

                    for (MaintenanceRequest req : all) {
                        Object[] row = {req.getId(), req.getLocation(), req.getCategory(),
                                req.getCreatedAt().format(DF), req.getStatus()};

                        switch (req.getStatus().toUpperCase()) {
                            case "PENDING":
                                pendingModel.addRow(row);
                                break;
                            case "APPROVED":
                                approvedModel.addRow(row);
                                break;
                            case "REJECTED":
                                if (rejectedCount < 2) {
                                    rejectedModel.addRow(row);
                                    rejectedCount++;
                                }
                                break;
                            case "COMPLETED":
                                if (completedCount < 2) {
                                    completedModel.addRow(row);
                                    completedCount++;
                                }
                                break;
                        }
                    }
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(FacultyDashboard.this,
                                    "Error loading data: " + ex.getMessage(), "Database Error",
                                    JOptionPane.ERROR_MESSAGE));
                }
                return null;
            }
        };
        worker.execute();
    }

    private void showNewRequestDialog() {
        JTextField locationField = new JTextField();
        locationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        locationField.setPreferredSize(new Dimension(250, 35));

        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
                "Furniture", "Electrical", "Fan/Lighting", "Projector/Equipment", "Other"
        });
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryCombo.setPreferredSize(new Dimension(250, 35));

        JTextArea descArea = new JTextArea(5, 25);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                new EmptyBorder(5, 8, 5, 8)
        ));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PANEL_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Submit New Maintenance Request");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Room/Location:"), gbc);
        gbc.gridx = 1;
        panel.add(locationField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        panel.add(categoryCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(descArea), gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "New Maintenance Request",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                requestService.submitRequest(faculty,
                        locationField.getText(),
                        (String) categoryCombo.getSelectedItem(),
                        descArea.getText());
                JOptionPane.showMessageDialog(this, "Request submitted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Submission Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDetails(int requestId) {
        List<MaintenanceRequest> all = requestService.getFacultyRequests(faculty);
        MaintenanceRequest req = all.stream()
                .filter(r -> r.getId() == requestId)
                .findFirst()
                .orElse(null);

        if (req == null) {
            JOptionPane.showMessageDialog(this, "Request not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder details = new StringBuilder();
        details.append("<html><body style='font-family:Segoe UI; font-size:14px;'>");
        details.append("<b>Request ID:</b> ").append(req.getId()).append("<br>");
        details.append("<b>Location:</b> ").append(req.getLocation()).append("<br>");
        details.append("<b>Category:</b> ").append(req.getCategory()).append("<br>");
        details.append("<b>Description:</b> ").append(req.getDescription()).append("<br>");
        details.append("<b>Status:</b> ").append(req.getStatus()).append("<br>");
        details.append("<b>Submitted:</b> ").append(req.getCreatedAt().format(DF)).append("<br>");
        if (req.getHodRemarks() != null && !req.getHodRemarks().trim().isEmpty()) {
            details.append("<b>HOD Remarks:</b> ").append(req.getHodRemarks()).append("<br>");
        }
        details.append("</body></html>");

        JOptionPane.showMessageDialog(this, details.toString(),
                "Request #" + req.getId() + " Details", JOptionPane.INFORMATION_MESSAGE);
    }
}
