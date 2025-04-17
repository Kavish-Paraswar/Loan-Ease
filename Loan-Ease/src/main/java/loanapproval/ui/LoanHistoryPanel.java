package loanapproval.ui;

import loanapproval.controller.LoanApplicationController;
import loanapproval.controller.LoanDecisionController;
import loanapproval.controller.UserController;
import loanapproval.model.LoanApplication;
import loanapproval.model.LoanDecision;
import loanapproval.model.LoanApplication.LoanStatus;
import loanapproval.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Panel that displays loan application history for a user.
 */
public class LoanHistoryPanel extends JPanel {
    private User currentUser;
    private JTable loanTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton viewDetailsButton;
    private JComboBox<String> statusFilterComboBox;
    
    private LoanApplicationController loanAppController;
    private LoanDecisionController loanDecisionController;
    private UserController userController;
    
    /**
     * Create a new LoanHistoryPanel.
     * @param currentUser The current logged-in user
     */
    public LoanHistoryPanel(User currentUser) {
        this.currentUser = currentUser;
        this.loanAppController = new LoanApplicationController();
        this.loanDecisionController = new LoanDecisionController();
        this.userController = new UserController();
        
        setLayout(new BorderLayout());
        
        initComponents();
        layoutComponents();
        setupListeners();
        loadLoanApplications(null); // Load all applications initially
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        // Create table model with column names
        String[] columnNames = {"ID", "Date", "Amount", "Purpose", "Duration", "Interest Rate", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        loanTable = new JTable(tableModel);
        loanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loanTable.setRowHeight(25);
        
        // Center alignment for columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < loanTable.getColumnCount(); i++) {
            loanTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Set custom renderer for status column with color coding
        loanTable.getColumnModel().getColumn(6).setCellRenderer(new StatusRenderer());
        
        // Status filter
        statusFilterComboBox = new JComboBox<>(new String[] {
            "All Statuses", 
            "Pending", 
            "Approved",
            "Rejected",
            "Cancelled"
        });
        
        // Buttons
        refreshButton = new JButton("Refresh");
        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setEnabled(false); // Disable initially
    }
    
    /**
     * Layout the UI components.
     */
    private void layoutComponents() {
        // Top panel with filters and buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Filter by Status:"));
        topPanel.add(statusFilterComboBox);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(refreshButton);
        topPanel.add(viewDetailsButton);
        
        // Table with scroll pane
        JScrollPane scrollPane = new JScrollPane(loanTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        loanTable.setFillsViewportHeight(true); // Ensure table fills viewport
        loanTable.setVisible(true); // Explicitly set table to visible
        
        // Add to main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Set preferred size
        setPreferredSize(new Dimension(800, 400));
    }
    
    /**
     * Set up action listeners for components.
     */
    private void setupListeners() {
        // Refresh button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedStatus = (String) statusFilterComboBox.getSelectedItem();
                LoanApplication.LoanStatus status = null;
                
                if (!"All Statuses".equals(selectedStatus)) {
                    status = LoanApplication.LoanStatus.valueOf(selectedStatus.toUpperCase());
                }
                
                loadLoanApplications(status);
            }
        });
        
        // View details button
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewSelectedLoanDetails();
            }
        });
        
        // Table selection listener
        loanTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                viewDetailsButton.setEnabled(loanTable.getSelectedRow() != -1);
            }
        });
        
        // Double-click on table row
        loanTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && loanTable.getSelectedRow() != -1) {
                    viewSelectedLoanDetails();
                }
            }
        });
        
        // Status filter
        statusFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedStatus = (String) statusFilterComboBox.getSelectedItem();
                LoanApplication.LoanStatus status = null;
                
                if (!"All Statuses".equals(selectedStatus)) {
                    status = LoanApplication.LoanStatus.valueOf(selectedStatus.toUpperCase());
                }
                
                loadLoanApplications(status);
            }
        });
    }
    
    /**
     * Load loan applications for the current user.
     * @param statusFilter The status to filter by, or null for all statuses
     */
    public void loadLoanApplications(LoanApplication.LoanStatus statusFilter) {
        // Clear the table
        tableModel.setRowCount(0);
        
        // Get loan applications for the current user
        List<LoanApplication> applications;
        if (statusFilter == null) {
            applications = loanAppController.getLoanApplicationsByUserId(currentUser.getUserId());
        } else {
            // Get all applications for the user, then filter by status
            applications = loanAppController.getLoanApplicationsByUserId(currentUser.getUserId());
            // Use Java streams to filter the list
            applications = applications.stream()
                .filter(app -> app.getStatus() == statusFilter)
                .collect(java.util.stream.Collectors.toList());
        }
        
        // Format for currency and dates
        DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
        DecimalFormat percentFormat = new DecimalFormat("0.00%");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // Add applications to the table
        for (LoanApplication app : applications) {
            Object[] rowData = {
                app.getApplicationId(),
                dateFormat.format(app.getApplicationDate()),
                currencyFormat.format(app.getLoanAmount()),
                app.getLoanPurpose(),
                app.getDurationMonths() + " months",
                percentFormat.format(app.getInterestRate()),
                app.getStatus().toString()
            };
            tableModel.addRow(rowData);
        }
        
        // Auto-resize columns
        loanTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    /**
     * View details of the selected loan application.
     */
    private void viewSelectedLoanDetails() {
        int selectedRow = loanTable.getSelectedRow();
        if (selectedRow != -1) {
            int applicationId = (int) loanTable.getValueAt(selectedRow, 0);
            LoanApplication application = loanAppController.getLoanApplicationById(applicationId);
            
            if (application != null) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                LoanApplicationDetailsDialog dialog = new LoanApplicationDetailsDialog(parentFrame, application);
                dialog.setVisible(true);
            }
        }
    }
    
    /**
     * Custom renderer for the status column.
     */
    private class StatusRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        
        public StatusRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component comp = super.getTableCellRendererComponent(table, value, 
                    isSelected, hasFocus, row, column);
            
            if (value != null) {
                String status = value.toString();
                
                // Set background color based on status
                if (status.equalsIgnoreCase("APPROVED")) {
                    comp.setBackground(isSelected ? new Color(100, 180, 100) : new Color(200, 250, 200));
                    comp.setForeground(Color.BLACK);
                } else if (status.equalsIgnoreCase("REJECTED")) {
                    comp.setBackground(isSelected ? new Color(180, 100, 100) : new Color(250, 200, 200));
                    comp.setForeground(Color.BLACK);
                } else if (status.equalsIgnoreCase("PENDING")) {
                    comp.setBackground(isSelected ? new Color(180, 180, 100) : new Color(250, 250, 200));
                    comp.setForeground(Color.BLACK);
                } else if (status.equalsIgnoreCase("CANCELLED")) {
                    comp.setBackground(isSelected ? new Color(150, 150, 150) : new Color(230, 230, 230));
                    comp.setForeground(Color.BLACK);
                } else {
                    comp.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    comp.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                }
            }
            
            return comp;
        }
    }
} 