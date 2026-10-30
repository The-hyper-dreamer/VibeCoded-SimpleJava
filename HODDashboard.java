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

public class HODDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private final User hod;
    private final RequestService requestService = new RequestService();
    private DefaultTableModel pendingModel;
    private DefaultTableModel approvedModel;

    // Color palette
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color PRIMARY_DARK = new Color(31, 97, 141);
    private static final Color BG_COLOR = new Color(245, 247, 250);
    private static final Color PANEL_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Color LABEL_COLOR = new Color(80, 80, 80);
    private static final Color SUCCESS_COLOR = new Color(46, 125, 50);
    private static final Color DANGER_COLOR = new Color(183, 28, 28);
    private static final Color INFO_COLOR = new Color(25, 118, 210);
    private static final Color STATUS_PENDING = new Color(241, 196, 15);
    private static final Color STATUS_APPROVED = new Color(46, 204, 113);
    private static final Color TABLE_HEADER_COLOR = new Color(41, 128, 185);

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");

    public HODDashboard(User hod) {
        this.hod = hod;
        setTitle("HOD Dashboard - " + hod.getUsername());
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
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

        JLabel titleLabel = new JLabel("HOD Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("Logged in as: " + hod.getUsername() + "  |  Role: HOD");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(200, 220, 240));

        JLabel roleBadge = new JLabel("HOD");
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

        pendingModel = new DefaultTableModel(
                new String[]{"ID", "Submitted By", "Location", "Category", "Submitted", "Status"}, 0);
        approvedModel = new DefaultTableModel(
                new String[]{"ID", "Submitted By", "Location", "Category", "Submitted", "Status"}, 0);

        tabbedPane.addTab("Pending Requests", createTablePanel(pendingModel, STATUS_PENDING));
        tabbedPane.addTab("Approved Awaiting Completion", createTablePanel(approvedModel, STATUS_APPROVED));

        add(tabbedPane, BorderLayout.CENTER);

        // Bottom action panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(PANEL_COLOR);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.setBackground(PRIMARY_COLOR);
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setPreferredSize(new Dimension(100, 40));
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshBtn.setBackground(PRIMARY_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshBtn.setBackground(PRIMARY_COLOR);
            }
        });
        refreshBtn.addActionListener(e -> refreshData());
        bottomPanel.add(refreshBtn, BorderLayout.WEST);

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
        table.setRowHeight(32);
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

                if (column == 5 && value != null) {
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
                            setBackground(new Color(192, 57, 43));
                            break;
                        case "COMPLETED":
                            setBackground(new Color(52, 73, 94));
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
                String status = (String) model.getValueAt(row, 5);
                showDetails(id, status);
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
                    List<MaintenanceRequest> all = requestService.getAllRequests(hod);
                    pendingModel.setRowCount(0);
                    approvedModel.setRowCount(0);

                    for (MaintenanceRequest req : all) {
                        Object[] row = {req.getId(), req.getFacultyName(), req.getLocation(),
                                req.getCategory(), req.getCreatedAt().format(DF), req.getStatus()};

                        switch (req.getStatus().toUpperCase()) {
                            case "PENDING":
                                pendingModel.addRow(row);
                                break;
                            case "APPROVED":
                                approvedModel.addRow(row);
                                break;
                        }
                    }
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(HODDashboard.this,
                                    "Error loading data: " + ex.getMessage(), "Database Error",
                                    JOptionPane.ERROR_MESSAGE));
                }
                return null;
            }
        };
        worker.execute();
    }

    private void showDetails(int requestId, String currentStatus) {
        List<MaintenanceRequest> all = requestService.getAllRequests(hod);
        MaintenanceRequest req = all.stream()
                .filter(r -> r.getId() == requestId)
                .findFirst()
                .orElse(null);

        if (req == null) {
            JOptionPane.showMessageDialog(this, "Request not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextArea remarksArea = new JTextArea(4, 35);
        remarksArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        remarksArea.setText(req.getHodRemarks() != null ? req.getHodRemarks() : "");
        remarksArea.setLineWrap(true);
        remarksArea.setWrapStyleWord(true);
        remarksArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                new EmptyBorder(5, 8, 5, 8)
        ));

        StringBuilder info = new StringBuilder();
        info.append("<html><body style='font-family:Segoe UI; font-size:14px;'>");
        info.append("<b>Request ID:</b> ").append(req.getId()).append("<br>");
        info.append("<b>Submitted By:</b> ").append(req.getFacultyName()).append("<br>");
        info.append("<b>Location:</b> ").append(req.getLocation()).append("<br>");
        info.append("<b>Category:</b> ").append(req.getCategory()).append("<br>");
        info.append("<b>Description:</b> ").append(req.getDescription()).append("<br>");
        info.append("<b>Status:</b> ").append(req.getStatus()).append("<br>");
        info.append("<b>Submitted:</b> ").append(req.getCreatedAt().format(DF)).append("<br>");
        info.append("</body></html>");

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel(info.toString()), BorderLayout.NORTH);

        JPanel remarksPanel = new JPanel(new BorderLayout(5, 5));
        remarksPanel.setBackground(PANEL_COLOR);
        remarksPanel.add(new JLabel("HOD Remarks:"), BorderLayout.NORTH);
        remarksPanel.add(new JScrollPane(remarksArea), BorderLayout.CENTER);

        panel.add(remarksPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(PANEL_COLOR);

        if ("PENDING".equalsIgnoreCase(currentStatus)) {
            JButton approveBtn = new JButton("Approve");
            approveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            approveBtn.setBackground(SUCCESS_COLOR);
            approveBtn.setForeground(Color.WHITE);
            approveBtn.setFocusPainted(false);
            approveBtn.setBorderPainted(false);
            approveBtn.setPreferredSize(new Dimension(110, 35));
            approveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            approveBtn.addActionListener(e -> {
                String remarks = remarksArea.getText().trim();
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Approve request #" + requestId + "?", "Confirm Approval",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        requestService.approveRequest(hod, requestId, remarks);
                        dispose();
                        new HODDashboard(hod).setVisible(true);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            buttonPanel.add(approveBtn);

            JButton rejectBtn = new JButton("Reject");
            rejectBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            rejectBtn.setBackground(DANGER_COLOR);
            rejectBtn.setForeground(Color.WHITE);
            rejectBtn.setFocusPainted(false);
            rejectBtn.setBorderPainted(false);
            rejectBtn.setPreferredSize(new Dimension(110, 35));
            rejectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            rejectBtn.addActionListener(e -> {
                String remarks = remarksArea.getText().trim();
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Reject request #" + requestId + "?", "Confirm Rejection",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        requestService.rejectRequest(hod, requestId, remarks);
                        dispose();
                        new HODDashboard(hod).setVisible(true);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            buttonPanel.add(rejectBtn);
        } else if ("APPROVED".equalsIgnoreCase(currentStatus)) {
            JButton completeBtn = new JButton("Mark Completed");
            completeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            completeBtn.setBackground(INFO_COLOR);
            completeBtn.setForeground(Color.WHITE);
            completeBtn.setFocusPainted(false);
            completeBtn.setBorderPainted(false);
            completeBtn.setPreferredSize(new Dimension(140, 35));
            completeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            completeBtn.addActionListener(e -> {
                String remarks = remarksArea.getText().trim();
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Mark request #" + requestId + " as completed?", "Confirm Completion",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        requestService.completeRequest(hod, requestId, remarks);
                        dispose();
                        new HODDashboard(hod).setVisible(true);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            buttonPanel.add(completeBtn);
        }

        JButton updateRemarksBtn = new JButton("Update Remarks");
        updateRemarksBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        updateRemarksBtn.setBackground(PRIMARY_COLOR);
        updateRemarksBtn.setForeground(Color.WHITE);
        updateRemarksBtn.setFocusPainted(false);
        updateRemarksBtn.setBorderPainted(false);
        updateRemarksBtn.setPreferredSize(new Dimension(130, 35));
        updateRemarksBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateRemarksBtn.addActionListener(e -> {
            String remarks = remarksArea.getText().trim();
            try {
                requestService.updateRemarks(hod, requestId, remarks);
                JOptionPane.showMessageDialog(this, "Remarks updated successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(updateRemarksBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel,
                "Request #" + requestId + " Details", JOptionPane.PLAIN_MESSAGE);
    }
}
