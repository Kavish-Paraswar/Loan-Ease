package loanapproval.ui;

import loanapproval.controller.LoanApplicationController;
import loanapproval.controller.UserController;
import loanapproval.model.LoanApplication;
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
 * Dashboard panel for regular users.
 */
public class UserDashboardPanel extends JPanel {
    private MainFrame parent;
    private LoanApplicationController loanController;
    private UserController userController;
    
    // UI Components
    private JLabel welcomeLabel;
    private JLabel userRoleLabel;
    private JTable loanApplicationsTable;
    private DefaultTableModel tableModel;
    private JButton newLoanButton;
    private JButton profileButton;
    private JButton refreshButton;
    private JLabel loansCountLabel;
    
    // Modern Colors
    private final Color PRIMARY_COLOR = new Color(25, 118, 210); // Material Blue
    private final Color SECONDARY_COLOR = new Color(33, 150, 243); // Lighter Blue
    private final Color ACCENT_COLOR = new Color(76, 175, 80); // Material Green
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light Gray
    private final Color CARD_BACKGROUND = new Color(255, 255, 255); // White
    private final Color TEXT_COLOR = new Color(33, 33, 33); // Almost Black
    private final Color HEADER_GRADIENT_START = new Color(25, 118, 210);
    private final Color HEADER_GRADIENT_END = new Color(21, 101, 192);
    private final Color TABLE_STRIPE_COLOR = new Color(248, 249, 250);
    private final Color TABLE_HOVER_COLOR = new Color(232, 240, 254);
    
    /**
     * Create a new UserDashboardPanel.
     * @param parent The parent MainFrame
     */
    public UserDashboardPanel(MainFrame parent) {
        this.parent = parent;
        this.loanController = new LoanApplicationController();
        this.userController = new UserController();
        
        setBackground(BACKGROUND_COLOR);
        
        initComponents();
        layoutComponents();
        setupListeners();
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        // Create header labels with modern styling
        welcomeLabel = new JLabel("Welcome, User");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        
        userRoleLabel = new JLabel("User Dashboard");
        userRoleLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 16));
        userRoleLabel.setForeground(new Color(255, 255, 255, 220));
        
        loansCountLabel = new JLabel("You have 0 loan applications");
        loansCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        loansCountLabel.setForeground(TEXT_COLOR);
        
        // Create table model with columns
        String[] columnNames = {"Application ID", "Amount", "Purpose", "Date", "Duration (Months)", "Status"};
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
        int[] columnWidths = {130, 150, 200, 120, 130, 120};
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
                if (column == 5 && value != null) {
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
        
        // Create modern styled buttons
        Font buttonFont = new Font("Segoe UI Semibold", Font.PLAIN, 14);
        
        newLoanButton = createStyledButton("Apply for Loan", buttonFont, ACCENT_COLOR);
        profileButton = createStyledButton("Manage Profile", buttonFont, PRIMARY_COLOR);
        refreshButton = createStyledButton("Refresh", buttonFont, SECONDARY_COLOR);
    }
    
    /**
     * Create a modern styled button with hover effect
     */
    private JButton createStyledButton(String text, Font font, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient paint for button
                GradientPaint gradient;
                if (getModel().isPressed()) {
                    gradient = new GradientPaint(0, 0, color.darker(), 0, getHeight(), color.darker().darker());
                } else if (getModel().isRollover()) {
                    gradient = new GradientPaint(0, 0, color.brighter(), 0, getHeight(), color);
                } else {
                    gradient = new GradientPaint(0, 0, color, 0, getHeight(), color.darker());
                }
                
                g2.setPaint(gradient);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                
                // Add subtle shadow effect
                if (!getModel().isPressed()) {
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fill(new RoundRectangle2D.Double(0, 2, getWidth(), getHeight(), 12, 12));
                }
                
                // Draw text with shadow
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                
                // Draw text shadow
                g2.setColor(new Color(0, 0, 0, 50));
                g2.drawString(getText(), textX + 1, textY + 1);
                
                // Draw text
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), textX, textY);
                
                g2.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += 40;
                size.height = 45;
                return size;
            }
        };
        
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
                
                GradientPaint gradient = new GradientPaint(0, 0, HEADER_GRADIENT_START, 
                        0, getHeight(), HEADER_GRADIENT_END);
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
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(profileButton);
        buttonPanel.add(newLoanButton);
        buttonPanel.add(refreshButton);
        
        headerContent.add(titlePanel, BorderLayout.WEST);
        headerContent.add(buttonPanel, BorderLayout.EAST);
        headerPanel.add(headerContent, BorderLayout.CENTER);
        
        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Create card panel for loan count
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
        countPanel.add(loansCountLabel);
        
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
        
        contentPanel.add(countPanel, BorderLayout.NORTH);
        contentPanel.add(tableCard, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Set up action listeners for the buttons.
     */
    private void setupListeners() {
        newLoanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNewLoanDialog();
            }
        });
        
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showProfileDialog();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        
        // Add double-click listener to table
        loanApplicationsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = loanApplicationsTable.getSelectedRow();
                    if (row >= 0) {
                        int applicationId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                        showLoanDetailsDialog(applicationId);
                    }
                }
            }
        });
    }
    
    /**
     * Refresh the dashboard data.
     */
    public void refreshData() {
        // Update welcome message
        User user = parent.getLoggedInUser();
        if (user != null) {
            welcomeLabel.setText("Welcome, " + user.getFullName());
            
            // Clear table
            tableModel.setRowCount(0);
            
            // Load user's loan applications
            List<LoanApplication> applications = loanController.getLoanApplicationsByUserId(user.getUserId());
            
            // Update count label
            loansCountLabel.setText("You have " + applications.size() + " loan " + 
                                   (applications.size() == 1 ? "application" : "applications"));
            
            // Format date for display
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            // Add applications to table
            for (LoanApplication app : applications) {
                Object[] row = {
                    app.getApplicationId(),
                    String.format("₹%.2f", app.getLoanAmount()),
                    app.getLoanPurpose(),
                    dateFormat.format(app.getApplicationDate()),
                    app.getDurationMonths(),
                    app.getStatus().toString()
                };
                tableModel.addRow(row);
            }
            
            // Repaint the table for updated styling
            loanApplicationsTable.repaint();
        }
    }
    
    /**
     * Reset the panel.
     */
    public void resetPanel() {
        welcomeLabel.setText("Welcome, User");
        loansCountLabel.setText("You have 0 loan applications");
        tableModel.setRowCount(0);
    }
    
    /**
     * Show dialog for creating a new loan application.
     */
    private void showNewLoanDialog() {
        NewLoanApplicationDialog dialog = new NewLoanApplicationDialog(parent, parent.getLoggedInUser());
        dialog.setVisible(true);
        
        // Refresh data if a new application was created
        if (dialog.isApplicationCreated()) {
            refreshData();
        }
    }
    
    /**
     * Show dialog for viewing and editing user profile.
     */
    private void showProfileDialog() {
        UserProfileDialog dialog = new UserProfileDialog(parent, parent.getLoggedInUser());
        dialog.setVisible(true);
        
        // Refresh data if profile was updated
        if (dialog.isProfileUpdated()) {
            // Update user in parent frame
            parent.setLoggedInUser(dialog.getUpdatedUser());
            
            // Refresh welcome message
            welcomeLabel.setText("Welcome, " + parent.getLoggedInUser().getFullName());
        }
    }
    
    /**
     * Show dialog with details of a loan application.
     * @param applicationId The ID of the application to view
     */
    private void showLoanDetailsDialog(int applicationId) {
        LoanApplication application = loanController.getLoanApplicationById(applicationId);
        if (application != null) {
            LoanApplicationDetailsDialog dialog = new LoanApplicationDetailsDialog(parent, application);
            dialog.setVisible(true);
        }
    }
} 