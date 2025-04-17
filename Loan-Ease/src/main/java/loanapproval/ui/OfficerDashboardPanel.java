package loanapproval.ui;

import loanapproval.controller.LoanApplicationController;
import loanapproval.controller.LoanDecisionController;
import loanapproval.model.LoanApplication;
import loanapproval.model.LoanApplication.LoanStatus;
import loanapproval.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Dashboard panel for loan officers.
 */
public class OfficerDashboardPanel extends JPanel {
    private MainFrame parent;
    private LoanApplicationController loanController;
    private LoanDecisionController decisionController;
    
    // UI Components
    private JLabel welcomeLabel;
    private JLabel userRoleLabel;
    private JTable loanApplicationsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilterCombo;
    private JButton refreshButton;
    private JButton viewButton;
    private JButton approveButton;
    private JButton rejectButton;
    private JLabel applicationsCountLabel;
    
    // Colors
    private final Color PRIMARY_COLOR = new Color(25, 118, 210); // Material Blue 
    private final Color SECONDARY_COLOR = new Color(33, 150, 243); // Lighter Blue
    private final Color ACCENT_COLOR = new Color(76, 175, 80); // Material Green
    private final Color DANGER_COLOR = new Color(211, 47, 47); // Material Red
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light Gray
    private final Color CARD_BACKGROUND = new Color(255, 255, 255); // White
    private final Color TEXT_COLOR = new Color(33, 33, 33); // Almost Black
    private final Color TABLE_STRIPE_COLOR = new Color(248, 249, 250);
    private final Color TABLE_HOVER_COLOR = new Color(232, 240, 254);
    
    /**
     * Create a new OfficerDashboardPanel.
     * @param parent The parent MainFrame
     */
    public OfficerDashboardPanel(MainFrame parent) {
        this.parent = parent;
        this.loanController = new LoanApplicationController();
        this.decisionController = new LoanDecisionController();
        
        setBackground(BACKGROUND_COLOR);
        
        initComponents();
        layoutComponents();
        setupListeners();
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        // Create header labels with custom styling
        welcomeLabel = new JLabel("Welcome, Officer");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        
        userRoleLabel = new JLabel("Loan Officer Dashboard");
        userRoleLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 16));
        userRoleLabel.setForeground(new Color(255, 255, 255, 220));
        
        applicationsCountLabel = new JLabel("0 loan applications found");
        applicationsCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        applicationsCountLabel.setForeground(TEXT_COLOR);
        
        // Create table model with columns
        String[] columnNames = {"Application ID", "User", "Amount", "Purpose", "Date", "Duration (Months)", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create basic table first
        loanApplicationsTable = new JTable(tableModel);
        
        // Basic table settings
        loanApplicationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loanApplicationsTable.setRowHeight(50);
        loanApplicationsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loanApplicationsTable.setBackground(Color.WHITE);
        loanApplicationsTable.setGridColor(new Color(230, 230, 230));
        loanApplicationsTable.setShowGrid(true);
        loanApplicationsTable.setShowHorizontalLines(true);
        loanApplicationsTable.setShowVerticalLines(false);
        
        // Set column widths
        int[] columnWidths = {120, 150, 150, 150, 120, 130, 120};
        for (int i = 0; i < columnWidths.length; i++) {
            loanApplicationsTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
            loanApplicationsTable.getColumnModel().getColumn(i).setMinWidth(columnWidths[i]);
        }
        
        // Create a custom header renderer
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                label.setBackground(SECONDARY_COLOR);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        };
        
        // Apply the header renderer to all columns
        JTableHeader header = loanApplicationsTable.getTableHeader();
        header.setDefaultRenderer(headerRenderer);
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        
        // Cell renderer for the table content
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    c.setBackground(TABLE_HOVER_COLOR);
                    c.setForeground(PRIMARY_COLOR);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_STRIPE_COLOR);
                    c.setForeground(TEXT_COLOR);
                }
                
                // Status column styling
                if (column == 6 && value != null) {
                    String status = value.toString();
                    JLabel label = (JLabel) c;
                    
                    if (status.equals("APPROVED")) {
                        label.setForeground(new Color(27, 94, 32));
                        label.setBackground(new Color(200, 230, 201));
                    } else if (status.equals("REJECTED")) {
                        label.setForeground(new Color(198, 40, 40));
                        label.setBackground(new Color(255, 205, 210));
                    } else {
                        label.setForeground(new Color(255, 111, 0));
                        label.setBackground(new Color(255, 224, 178));
                    }
                    label.setOpaque(true);
                }
                
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                return c;
            }
        };
        
        // Apply the cell renderer to all columns
        for (int i = 0; i < loanApplicationsTable.getColumnCount(); i++) {
            loanApplicationsTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        // Create styled filter combo box
        String[] statusOptions = {"All Applications", "Pending", "Approved", "Rejected"};
        statusFilterCombo = new JComboBox<>(statusOptions);
        statusFilterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusFilterCombo.setBackground(Color.WHITE);
        statusFilterCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        
        // Create styled buttons
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        
        refreshButton = createStyledButton("Refresh", buttonFont, SECONDARY_COLOR);
        // Commented out to avoid NullPointerException since icons are not available
        // refreshButton.setIcon(new ImageIcon(getClass().getResource("/images/refresh.png")));
        
        viewButton = createStyledButton("View Details", buttonFont, PRIMARY_COLOR);
        // viewButton.setIcon(new ImageIcon(getClass().getResource("/images/view.png")));
        
        approveButton = createStyledButton("Approve", buttonFont, ACCENT_COLOR);
        // approveButton.setIcon(new ImageIcon(getClass().getResource("/images/approve.png")));
        
        rejectButton = createStyledButton("Reject", buttonFont, DANGER_COLOR);
        // rejectButton.setIcon(new ImageIcon(getClass().getResource("/images/reject.png")));
        
        // Initial button states
        viewButton.setEnabled(false);
        approveButton.setEnabled(false);
        rejectButton.setEnabled(false);
    }
    
    /**
     * Create a styled button with rounded corners and hover effect
     */
    private JButton createStyledButton(String text, Font font, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (!isEnabled()) {
                    g2.setColor(new Color(200, 200, 200));
                } else if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }
                
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
                
                g2.setColor(isEnabled() ? Color.WHITE : new Color(150, 150, 150));
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                
                // Calculate text position - account for icon if present
                int textX;
                if (getIcon() != null) {
                    int iconTextGap = getIconTextGap();
                    int iconWidth = getIcon().getIconWidth();
                    int textWidth = fm.stringWidth(getText());
                    textX = (getWidth() - (iconWidth + iconTextGap + textWidth)) / 2 + iconWidth + iconTextGap;
                } else {
                    textX = (getWidth() - fm.stringWidth(getText())) / 2;
                }
                
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                
                // Draw icon if present
                if (getIcon() != null) {
                    int iconY = (getHeight() - getIcon().getIconHeight()) / 2;
                    int iconX = textX - getIconTextGap() - getIcon().getIconWidth();
                    getIcon().paintIcon(this, g2, iconX, iconY);
                }
                
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
            
            // These ensure the button is drawn properly
            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += 20;
                size.height = 40;
                return size;
            }
        };
        
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setIconTextGap(10);
        
        return button;
    }
    
    /**
     * Layout the UI components with modern styling
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(0, 0));
        
        // Create header panel with gradient
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(0, 0, new Color(25, 118, 210), 
                        0, getHeight(), new Color(21, 101, 192));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        headerPanel.setPreferredSize(new Dimension(getWidth(), 120));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Header content
        JPanel headerContent = new JPanel(new BorderLayout(15, 5));
        headerContent.setOpaque(false);
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 0));
        titlePanel.setOpaque(false);
        titlePanel.add(welcomeLabel);
        titlePanel.add(userRoleLabel);
        
        JPanel headerControlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        headerControlsPanel.setOpaque(false);
        headerControlsPanel.add(new JLabel("Filter:"));
        headerControlsPanel.add(statusFilterCombo);
        headerControlsPanel.add(refreshButton);
        
        headerContent.add(titlePanel, BorderLayout.WEST);
        headerContent.add(headerControlsPanel, BorderLayout.EAST);
        headerPanel.add(headerContent, BorderLayout.CENTER);
        
        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Create card panel for application count
        JPanel countPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw card background with shadow
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, 15, 15);
                g2d.setColor(CARD_BACKGROUND);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 15, 15);
            }
        };
        countPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        countPanel.setBackground(CARD_BACKGROUND);
        countPanel.add(applicationsCountLabel);
        
        // Create card panel for table
        JPanel tableCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw card background with shadow
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, 15, 15);
                g2d.setColor(CARD_BACKGROUND);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 15, 15);
            }
        };
        tableCard.setLayout(new BorderLayout());
        tableCard.setBackground(CARD_BACKGROUND);
        tableCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(loanApplicationsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.add(viewButton);
        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        tableCard.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(countPanel, BorderLayout.NORTH);
        contentPanel.add(tableCard, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Set up action listeners for the buttons and combobox.
     */
    private void setupListeners() {
        // Filter combo box listener
        statusFilterCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        
        // Refresh button listener
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        
        // View button listener
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSelectedApplication();
            }
        });
        
        // Approve button listener
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSelectedApplication(true);
            }
        });
        
        // Reject button listener
        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSelectedApplication(false);
            }
        });
        
        // Table selection listener to update button states
        loanApplicationsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
        
        // Double-click listener for table
        loanApplicationsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewSelectedApplication();
                }
            }
        });
    }
    
    /**
     * Refresh the dashboard data.
     */
    public void refreshData() {
        // Update welcome message
        User officer = parent.getLoggedInUser();
        if (officer != null) {
            welcomeLabel.setText("Welcome, " + officer.getFullName());
            
            // Clear table
            tableModel.setRowCount(0);
            
            // Get selected filter
            String selectedFilter = (String) statusFilterCombo.getSelectedItem();
            List<LoanApplication> applications;
            
            // Apply filter
            if (selectedFilter.equals("Pending")) {
                applications = loanController.getLoanApplicationsByStatus(LoanStatus.PENDING);
            } else if (selectedFilter.equals("Approved")) {
                applications = loanController.getLoanApplicationsByStatus(LoanStatus.APPROVED);
            } else if (selectedFilter.equals("Rejected")) {
                applications = loanController.getLoanApplicationsByStatus(LoanStatus.REJECTED);
            } else {
                // "All Applications"
                applications = loanController.getAllLoanApplications();
            }
            
            // Update applications count label
            applicationsCountLabel.setText(applications.size() + " loan " + 
                                  (applications.size() == 1 ? "application" : "applications") + " found");
            
            // Format date for display
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            // Add applications to table
            for (LoanApplication app : applications) {
                Object[] row = {
                    app.getApplicationId(),
                    app.getUserName(),
                    String.format("₹%.2f", app.getLoanAmount()),
                    app.getLoanPurpose(),
                    dateFormat.format(app.getApplicationDate()),
                    app.getDurationMonths(),
                    app.getStatus().toString()
                };
                tableModel.addRow(row);
            }
            
            // Update button states based on selection
            updateButtonStates();
            
            // Repaint the table for updated styling
            loanApplicationsTable.repaint();
        }
    }
    
    /**
     * Reset the panel.
     */
    public void resetPanel() {
        welcomeLabel.setText("Welcome, Officer");
        applicationsCountLabel.setText("0 loan applications found");
        tableModel.setRowCount(0);
        statusFilterCombo.setSelectedIndex(0);
        updateButtonStates();
    }
    
    /**
     * Update the enabled state of buttons based on selection.
     */
    private void updateButtonStates() {
        int selectedRow = loanApplicationsTable.getSelectedRow();
        boolean hasSelection = selectedRow >= 0;
        
        viewButton.setEnabled(hasSelection);
        
        if (hasSelection) {
            String status = (String) tableModel.getValueAt(selectedRow, 6);
            boolean isPending = status.equals(LoanStatus.PENDING.toString());
            
            approveButton.setEnabled(isPending);
            rejectButton.setEnabled(isPending);
        } else {
            approveButton.setEnabled(false);
            rejectButton.setEnabled(false);
        }
    }
    
    /**
     * View the selected loan application.
     */
    private void viewSelectedApplication() {
        int selectedRow = loanApplicationsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int applicationId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            LoanApplication application = loanController.getLoanApplicationById(applicationId);
            
            if (application != null) {
                OfficerLoanReviewDialog dialog = new OfficerLoanReviewDialog(
                        parent,
                        application,
                        parent.getLoggedInUser()
                );
                dialog.setVisible(true);
                
                // Refresh data if a decision was made
                if (dialog.isDecisionMade()) {
                    refreshData();
                }
            }
        }
    }
    
    /**
     * Process (approve or reject) the selected loan application.
     * @param approve true to approve, false to reject
     */
    private void processSelectedApplication(boolean approve) {
        int selectedRow = loanApplicationsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int applicationId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            String applicantName = tableModel.getValueAt(selectedRow, 1).toString();
            
            String action = approve ? "approve" : "reject";
            String title = approve ? "Approve Loan" : "Reject Loan";
            
            int response = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to " + action + " the loan application for " + applicantName + "?",
                    title,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            
            if (response == JOptionPane.YES_OPTION) {
                // Get comment from user
                String comment = JOptionPane.showInputDialog(
                        this,
                        "Please enter a comment (optional):",
                        title,
                        JOptionPane.PLAIN_MESSAGE
                );
                
                // Process decision
                boolean success = decisionController.processLoanApplication(
                        applicationId,
                        parent.getLoggedInUser().getUserId(),
                        approve,
                        comment
                );
                
                if (success) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Loan application has been " + (approve ? "approved" : "rejected") + ".",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "An error occurred while processing the loan decision.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
} 