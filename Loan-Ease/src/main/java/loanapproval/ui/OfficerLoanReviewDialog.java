package loanapproval.ui;

import loanapproval.controller.FinancialInfoController;
import loanapproval.controller.LoanApplicationController;
import loanapproval.controller.LoanDecisionController;
import loanapproval.controller.UserController;
import loanapproval.model.FinancialInfo;
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
 * Dialog for loan officers to review loan applications and make decisions.
 */
public class OfficerLoanReviewDialog extends JDialog {
    private JFrame parent;
    private LoanApplication application;
    private User officer;
    private FinancialInfo financialInfo;
    private User applicant;
    private LoanDecision previousDecision;
    
    private LoanApplicationController loanController;
    private UserController userController;
    private FinancialInfoController financialController;
    private LoanDecisionController decisionController;
    
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
    
    // Financial Info Components
    private JLabel incomeLabel;
    private JLabel employmentStatusLabel;
    private JLabel employerLabel;
    private JLabel jobTitleLabel;
    private JLabel employmentDurationLabel;
    private JLabel creditScoreLabel;
    private JLabel debtsLabel;
    private JLabel debtToIncomeLabel;
    
    // Decision Components
    private JRadioButton approveRadio;
    private JRadioButton rejectRadio;
    private JTextArea commentsArea;
    private JLabel previousDecisionLabel;
    private JLabel previousCommentsLabel;
    
    private JButton submitButton;
    private JButton cancelButton;
    
    private boolean decisionMade = false;
    
    /**
     * Create a new OfficerLoanReviewDialog.
     * @param parent The parent frame
     * @param application The loan application to review
     * @param officer The loan officer reviewing the application
     */
    public OfficerLoanReviewDialog(JFrame parent, LoanApplication application, User officer) {
        super(parent, "Loan Application Review", true);
        this.parent = parent;
        this.application = application;
        this.officer = officer;
        
        this.loanController = new LoanApplicationController();
        this.userController = new UserController();
        this.financialController = new FinancialInfoController();
        this.decisionController = new LoanDecisionController();
        
        // Load additional data
        loadRelatedData();
        
        initComponents();
        layoutComponents();
        populateData();
        setupListeners();
        
        pack();
        setSize(800, 600);
        setLocationRelativeTo(parent);
    }
    
    /**
     * Load related data for the loan application.
     */
    private void loadRelatedData() {
        applicant = userController.getUserById(application.getUserId());
        financialInfo = financialController.getFinancialInfoByUserId(application.getUserId());
        previousDecision = decisionController.getLoanDecisionByApplicationId(application.getApplicationId());
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        // Application Details Labels
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
        
        // Financial Info Labels
        incomeLabel = new JLabel();
        employmentStatusLabel = new JLabel();
        employerLabel = new JLabel();
        jobTitleLabel = new JLabel();
        employmentDurationLabel = new JLabel();
        creditScoreLabel = new JLabel();
        debtsLabel = new JLabel();
        debtToIncomeLabel = new JLabel();
        
        // Decision Components
        approveRadio = new JRadioButton("Approve");
        rejectRadio = new JRadioButton("Reject");
        
        ButtonGroup decisionGroup = new ButtonGroup();
        decisionGroup.add(approveRadio);
        decisionGroup.add(rejectRadio);
        
        commentsArea = new JTextArea(5, 30);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        
        previousDecisionLabel = new JLabel("No previous decision");
        previousCommentsLabel = new JLabel("N/A");
        
        // Buttons
        submitButton = new JButton("Submit Decision");
        cancelButton = new JButton("Cancel");
    }
    
    /**
     * Layout the UI components.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Application Details Panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        
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
        
        // Status
        gbc.gridx = 0;
        gbc.gridy = 3;
        applicationPanel.add(new JLabel("Status:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        applicationPanel.add(statusLabel, gbc);
        
        // Loan Details Panel
        JPanel loanPanel = new JPanel(new GridBagLayout());
        loanPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Loan Details",
                TitledBorder.LEFT,
                TitledBorder.TOP));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Loan Amount
        gbc.gridx = 0;
        gbc.gridy = 0;
        loanPanel.add(new JLabel("Loan Amount:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        loanPanel.add(loanAmountLabel, gbc);
        
        // Loan Purpose
        gbc.gridx = 0;
        gbc.gridy = 1;
        loanPanel.add(new JLabel("Purpose:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        loanPanel.add(loanPurposeLabel, gbc);
        
        // Duration
        gbc.gridx = 0;
        gbc.gridy = 2;
        loanPanel.add(new JLabel("Duration:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        loanPanel.add(durationLabel, gbc);
        
        // Interest Rate
        gbc.gridx = 0;
        gbc.gridy = 3;
        loanPanel.add(new JLabel("Interest Rate:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        loanPanel.add(interestRateLabel, gbc);
        
        // Monthly Payment
        gbc.gridx = 0;
        gbc.gridy = 4;
        loanPanel.add(new JLabel("Monthly Payment:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        loanPanel.add(monthlyPaymentLabel, gbc);
        
        // Total Payment
        gbc.gridx = 0;
        gbc.gridy = 5;
        loanPanel.add(new JLabel("Total Payment:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        loanPanel.add(totalPaymentLabel, gbc);
        
        // Combine both panels
        JPanel combinedPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        combinedPanel.add(applicationPanel);
        combinedPanel.add(loanPanel);
        detailsPanel.add(combinedPanel, BorderLayout.NORTH);
        
        // Financial Info Panel
        JPanel financialPanel = new JPanel(new GridBagLayout());
        financialPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Annual Income
        gbc.gridx = 0;
        gbc.gridy = 0;
        financialPanel.add(new JLabel("Annual Income:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        financialPanel.add(incomeLabel, gbc);
        
        // Employment Status
        gbc.gridx = 0;
        gbc.gridy = 1;
        financialPanel.add(new JLabel("Employment Status:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        financialPanel.add(employmentStatusLabel, gbc);
        
        // Employer
        gbc.gridx = 0;
        gbc.gridy = 2;
        financialPanel.add(new JLabel("Employer:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        financialPanel.add(employerLabel, gbc);
        
        // Job Title
        gbc.gridx = 0;
        gbc.gridy = 3;
        financialPanel.add(new JLabel("Job Title:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        financialPanel.add(jobTitleLabel, gbc);
        
        // Employment Duration
        gbc.gridx = 0;
        gbc.gridy = 4;
        financialPanel.add(new JLabel("Employment Duration:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        financialPanel.add(employmentDurationLabel, gbc);
        
        // Credit Score
        gbc.gridx = 0;
        gbc.gridy = 5;
        financialPanel.add(new JLabel("Credit Score:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        financialPanel.add(creditScoreLabel, gbc);
        
        // Existing Debts
        gbc.gridx = 0;
        gbc.gridy = 6;
        financialPanel.add(new JLabel("Existing Debts:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        financialPanel.add(debtsLabel, gbc);
        
        // Debt-to-Income Ratio
        gbc.gridx = 0;
        gbc.gridy = 7;
        financialPanel.add(new JLabel("Debt-to-Income Ratio:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 7;
        financialPanel.add(debtToIncomeLabel, gbc);
        
        // Decision Panel
        JPanel decisionPanel = new JPanel(new BorderLayout());
        decisionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel newDecisionPanel = new JPanel(new GridBagLayout());
        newDecisionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Make Decision",
                TitledBorder.LEFT,
                TitledBorder.TOP));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Decision Radio Buttons
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(approveRadio);
        radioPanel.add(rejectRadio);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        newDecisionPanel.add(radioPanel, gbc);
        
        // Comments
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        newDecisionPanel.add(new JLabel("Comments:"), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        newDecisionPanel.add(new JScrollPane(commentsArea), gbc);
        
        JPanel previousDecisionPanel = new JPanel(new GridBagLayout());
        previousDecisionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Previous Decision",
                TitledBorder.LEFT,
                TitledBorder.TOP));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Previous Decision
        gbc.gridx = 0;
        gbc.gridy = 0;
        previousDecisionPanel.add(new JLabel("Decision:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        previousDecisionPanel.add(previousDecisionLabel, gbc);
        
        // Previous Comments
        gbc.gridx = 0;
        gbc.gridy = 1;
        previousDecisionPanel.add(new JLabel("Comments:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        previousDecisionPanel.add(previousCommentsLabel, gbc);
        
        JPanel combinedDecisionPanel = new JPanel(new BorderLayout());
        combinedDecisionPanel.add(newDecisionPanel, BorderLayout.CENTER);
        combinedDecisionPanel.add(previousDecisionPanel, BorderLayout.SOUTH);
        
        decisionPanel.add(combinedDecisionPanel, BorderLayout.CENTER);
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Application Details", detailsPanel);
        tabbedPane.addTab("Financial Information", financialPanel);
        tabbedPane.addTab("Decision", decisionPanel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        // Add components to main panel
        add(tabbedPane, BorderLayout.CENTER);
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
        
        // Populate financial info
        if (financialInfo != null) {
            incomeLabel.setText(currencyFormat.format(financialInfo.getAnnualIncome()).replace("$", "₹"));
            employmentStatusLabel.setText(financialInfo.getEmploymentStatus());
            employerLabel.setText(financialInfo.getEmployerName());
            jobTitleLabel.setText(financialInfo.getJobTitle());
            employmentDurationLabel.setText(financialInfo.getEmploymentDuration() + " months");
            
            if (financialInfo.getCreditScore() > 0) {
                creditScoreLabel.setText(String.valueOf(financialInfo.getCreditScore()));
            } else {
                creditScoreLabel.setText("Not available");
            }
            
            debtsLabel.setText(currencyFormat.format(financialInfo.getExistingDebts()).replace("$", "₹"));
            
            // Calculate debt-to-income ratio
            double debtToIncome = financialInfo.calculateDebtToIncomeRatio();
            debtToIncomeLabel.setText(String.format("%.2f%%", debtToIncome));
            
            // Color code debt-to-income ratio
            if (debtToIncome < 20) {
                debtToIncomeLabel.setForeground(Color.GREEN.darker());
            } else if (debtToIncome < 36) {
                debtToIncomeLabel.setForeground(Color.BLUE);
            } else {
                debtToIncomeLabel.setForeground(Color.RED);
            }
        } else {
            // No financial info available
            incomeLabel.setText("Not available");
            employmentStatusLabel.setText("Not available");
            employerLabel.setText("Not available");
            jobTitleLabel.setText("Not available");
            employmentDurationLabel.setText("Not available");
            creditScoreLabel.setText("Not available");
            debtsLabel.setText("Not available");
            debtToIncomeLabel.setText("Not available");
        }
        
        // Populate previous decision
        if (previousDecision != null) {
            previousDecisionLabel.setText(previousDecision.getStatus().toString() + 
                    " on " + dateFormat.format(previousDecision.getDecisionDate()) + 
                    " by " + previousDecision.getOfficerName());
            previousCommentsLabel.setText(previousDecision.getComments());
            
            // Color code previous decision
            switch (previousDecision.getStatus()) {
                case APPROVED:
                    previousDecisionLabel.setForeground(Color.GREEN.darker());
                    break;
                case REJECTED:
                    previousDecisionLabel.setForeground(Color.RED);
                    break;
            }
        }
        
        // Enable/disable decision making based on application status
        boolean canMakeDecision = application.getStatus() == LoanApplication.LoanStatus.PENDING;
        approveRadio.setEnabled(canMakeDecision);
        rejectRadio.setEnabled(canMakeDecision);
        commentsArea.setEnabled(canMakeDecision);
        submitButton.setEnabled(canMakeDecision);
        
        // Pre-select a decision based on previous decision if available
        if (previousDecision != null && canMakeDecision) {
            switch (previousDecision.getStatus()) {
                case APPROVED:
                    approveRadio.setSelected(true);
                    break;
                case REJECTED:
                    rejectRadio.setSelected(true);
                    break;
            }
        }
    }
    
    /**
     * Set up action listeners for the buttons.
     */
    private void setupListeners() {
        // Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        // Submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitDecision();
            }
        });
    }
    
    /**
     * Submit the loan decision.
     */
    private void submitDecision() {
        // Check if a decision is selected
        if (!approveRadio.isSelected() && !rejectRadio.isSelected()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a decision (Approve or Reject)",
                    "Decision Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirm decision
        boolean isApproved = approveRadio.isSelected();
        String decision = isApproved ? "approve" : "reject";
        String decisionCap = isApproved ? "Approve" : "Reject";
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to " + decision + " this loan application?",
                "Confirm " + decisionCap, JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Process the application
            boolean success = decisionController.processLoanApplication(
                    application.getApplicationId(), 
                    officer.getUserId(), 
                    isApproved, 
                    commentsArea.getText());
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Loan application has been " + decision + "d successfully",
                        decisionCap + " Success", JOptionPane.INFORMATION_MESSAGE);
                decisionMade = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to " + decision + " the loan application",
                        decisionCap + " Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Check if a decision was made.
     * @return true if a decision was made, false otherwise
     */
    public boolean isDecisionMade() {
        return decisionMade;
    }
} 