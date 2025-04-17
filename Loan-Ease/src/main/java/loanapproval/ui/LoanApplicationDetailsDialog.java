package loanapproval.ui;

import loanapproval.controller.LoanDecisionController;
import loanapproval.controller.UserController;
import loanapproval.model.LoanApplication;
import loanapproval.model.LoanDecision;
import loanapproval.model.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Dialog for viewing loan application details.
 */
public class LoanApplicationDetailsDialog extends JDialog {
    private LoanApplication application;
    private LoanDecision decision;
    private User applicant;
    
    private LoanDecisionController decisionController;
    private UserController userController;
    
    // UI Components
    private JLabel applicationIdLabel;
    private JLabel applicantNameLabel;
    private JLabel applicationDateLabel;
    private JLabel loanAmountLabel;
    private JLabel loanPurposeLabel;
    private JLabel durationLabel;
    private JLabel interestRateLabel;
    private JLabel monthlyPaymentLabel;
    private JLabel totalPaymentLabel;
    private JLabel statusLabel;
    private JLabel decisionLabel;
    private JLabel decisionDateLabel;
    private JLabel officerNameLabel;
    private JTextArea commentsArea;
    
    private JButton closeButton;
    
    /**
     * Create a new LoanApplicationDetailsDialog.
     * @param parent The parent frame
     * @param application The loan application to view
     */
    public LoanApplicationDetailsDialog(JFrame parent, LoanApplication application) {
        super(parent, "Loan Application Details", true);
        this.application = application;
        this.decisionController = new LoanDecisionController();
        this.userController = new UserController();
        
        // Load additional data
        loadRelatedData();
        
        initComponents();
        layoutComponents();
        populateData();
        setupListeners();
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    /**
     * Load related data for the loan application.
     */
    private void loadRelatedData() {
        applicant = userController.getUserById(application.getUserId());
        decision = decisionController.getLoanDecisionByApplicationId(application.getApplicationId());
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        applicationIdLabel = new JLabel();
        applicantNameLabel = new JLabel();
        applicationDateLabel = new JLabel();
        loanAmountLabel = new JLabel();
        loanPurposeLabel = new JLabel();
        durationLabel = new JLabel();
        interestRateLabel = new JLabel();
        monthlyPaymentLabel = new JLabel();
        totalPaymentLabel = new JLabel();
        statusLabel = new JLabel();
        decisionLabel = new JLabel("N/A");
        decisionDateLabel = new JLabel("N/A");
        officerNameLabel = new JLabel("N/A");
        commentsArea = new JTextArea(5, 30);
        commentsArea.setEditable(false);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        
        closeButton = new JButton("Close");
    }
    
    /**
     * Layout the UI components.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Application panel
        JPanel applicationPanel = new JPanel(new GridBagLayout());
        applicationPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Application Details",
                TitledBorder.LEFT,
                TitledBorder.TOP));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Application ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        applicationPanel.add(new JLabel("Application ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        applicationPanel.add(applicationIdLabel, gbc);
        
        // Applicant Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        applicationPanel.add(new JLabel("Applicant:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        applicationPanel.add(applicantNameLabel, gbc);
        
        // Application Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        applicationPanel.add(new JLabel("Application Date:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        applicationPanel.add(applicationDateLabel, gbc);
        
        // Loan Amount
        gbc.gridx = 0;
        gbc.gridy = 3;
        applicationPanel.add(new JLabel("Loan Amount:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        applicationPanel.add(loanAmountLabel, gbc);
        
        // Loan Purpose
        gbc.gridx = 0;
        gbc.gridy = 4;
        applicationPanel.add(new JLabel("Purpose:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        applicationPanel.add(loanPurposeLabel, gbc);
        
        // Loan Terms panel
        JPanel termsPanel = new JPanel(new GridBagLayout());
        termsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Loan Terms",
                TitledBorder.LEFT,
                TitledBorder.TOP));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Duration
        gbc.gridx = 0;
        gbc.gridy = 0;
        termsPanel.add(new JLabel("Duration:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        termsPanel.add(durationLabel, gbc);
        
        // Interest Rate
        gbc.gridx = 0;
        gbc.gridy = 1;
        termsPanel.add(new JLabel("Interest Rate:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        termsPanel.add(interestRateLabel, gbc);
        
        // Monthly Payment
        gbc.gridx = 0;
        gbc.gridy = 2;
        termsPanel.add(new JLabel("Monthly Payment:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        termsPanel.add(monthlyPaymentLabel, gbc);
        
        // Total Payment
        gbc.gridx = 0;
        gbc.gridy = 3;
        termsPanel.add(new JLabel("Total Payment:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        termsPanel.add(totalPaymentLabel, gbc);
        
        // Status
        gbc.gridx = 0;
        gbc.gridy = 4;
        termsPanel.add(new JLabel("Status:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        termsPanel.add(statusLabel, gbc);
        
        // Decision panel
        JPanel decisionPanel = new JPanel(new GridBagLayout());
        decisionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Loan Decision",
                TitledBorder.LEFT,
                TitledBorder.TOP));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Decision
        gbc.gridx = 0;
        gbc.gridy = 0;
        decisionPanel.add(new JLabel("Decision:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        decisionPanel.add(decisionLabel, gbc);
        
        // Decision Date
        gbc.gridx = 0;
        gbc.gridy = 1;
        decisionPanel.add(new JLabel("Decision Date:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        decisionPanel.add(decisionDateLabel, gbc);
        
        // Officer
        gbc.gridx = 0;
        gbc.gridy = 2;
        decisionPanel.add(new JLabel("Officer:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        decisionPanel.add(officerNameLabel, gbc);
        
        // Comments
        gbc.gridx = 0;
        gbc.gridy = 3;
        decisionPanel.add(new JLabel("Comments:"), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        decisionPanel.add(new JScrollPane(commentsArea), gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(applicationPanel);
        mainPanel.add(termsPanel);
        mainPanel.add(decisionPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Populate the UI components with data.
     */
    private void populateData() {
        // Format currency and date
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // Populate application details
        applicationIdLabel.setText(String.valueOf(application.getApplicationId()));
        applicantNameLabel.setText(applicant.getFullName());
        applicationDateLabel.setText(dateFormat.format(application.getApplicationDate()));
        loanAmountLabel.setText(currencyFormat.format(application.getLoanAmount()).replace("$", "₹"));
        loanPurposeLabel.setText(application.getLoanPurpose());
        durationLabel.setText(application.getDurationMonths() + " months");
        interestRateLabel.setText(String.format("%.2f%%", application.getInterestRate()));
        
        // Calculate and set payment details
        double monthlyPayment = application.calculateMonthlyPayment();
        double totalPayment = application.calculateTotalPayment();
        
        monthlyPaymentLabel.setText(currencyFormat.format(monthlyPayment).replace("$", "₹"));
        totalPaymentLabel.setText(currencyFormat.format(totalPayment).replace("$", "₹"));
        
        // Set status with color indicator
        statusLabel.setText(application.getStatus().toString());
        switch (application.getStatus()) {
            case PENDING:
                statusLabel.setForeground(Color.BLUE);
                break;
            case APPROVED:
                statusLabel.setForeground(Color.GREEN.darker());
                break;
            case REJECTED:
                statusLabel.setForeground(Color.RED);
                break;
        }
        
        // Populate decision details if available
        if (decision != null) {
            decisionLabel.setText(decision.getStatus().toString());
            decisionDateLabel.setText(dateFormat.format(decision.getDecisionDate()));
            officerNameLabel.setText(decision.getOfficerName());
            commentsArea.setText(decision.getComments());
            
            // Color code decision
            switch (decision.getStatus()) {
                case APPROVED:
                    decisionLabel.setForeground(Color.GREEN.darker());
                    break;
                case REJECTED:
                    decisionLabel.setForeground(Color.RED);
                    break;
            }
        }
    }
    
    /**
     * Set up action listeners for the buttons.
     */
    private void setupListeners() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
} 