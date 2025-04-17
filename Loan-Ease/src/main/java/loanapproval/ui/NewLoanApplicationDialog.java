package loanapproval.ui;

import loanapproval.controller.FinancialInfoController;
import loanapproval.controller.LoanApplicationController;
import loanapproval.model.FinancialInfo;
import loanapproval.model.LoanApplication;
import loanapproval.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Hashtable;

/**
 * Dialog for creating a new loan application.
 */
public class NewLoanApplicationDialog extends JDialog {
    private JFrame parent;
    private User user;
    private LoanApplicationController loanController;
    private FinancialInfoController financialController;
    
    // UI Components
    private JTextField amountField;
    private JTextField purposeField;
    private JSlider durationSlider;
    private JLabel monthlyPaymentLabel;
    private JLabel totalPaymentLabel;
    private JLabel interestRateLabel;
    
    // Financial Info Components
    private JTextField incomeField;
    private JComboBox<String> employmentStatusCombo;
    private JTextField employerField;
    private JTextField jobTitleField;
    private JTextField employmentDurationField;
    private JTextField creditScoreField;
    private JTextField debtsField;
    
    private JButton submitButton;
    private JButton cancelButton;
    
    private boolean applicationCreated = false;
    
    /**
     * Create a new NewLoanApplicationDialog.
     * @param parent The parent frame
     * @param user The user applying for a loan
     */
    public NewLoanApplicationDialog(JFrame parent, User user) {
        super(parent, "Apply for a Loan", true);
        this.parent = parent;
        this.user = user;
        this.loanController = new LoanApplicationController();
        this.financialController = new FinancialInfoController();
        
        initComponents();
        layoutComponents();
        setupListeners();
        loadFinancialInfo();
        updatePaymentCalculation();
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        // Loan fields
        amountField = new JTextField(10);
        purposeField = new JTextField(20);
        
        durationSlider = new JSlider(JSlider.HORIZONTAL, 6, 60, 24);
        durationSlider.setMajorTickSpacing(12);
        durationSlider.setMinorTickSpacing(6);
        durationSlider.setPaintTicks(true);
        durationSlider.setPaintLabels(true);
        
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(6, new JLabel("6 mo"));
        labels.put(12, new JLabel("1 yr"));
        labels.put(24, new JLabel("2 yr"));
        labels.put(36, new JLabel("3 yr"));
        labels.put(48, new JLabel("4 yr"));
        labels.put(60, new JLabel("5 yr"));
        durationSlider.setLabelTable(labels);
        
        monthlyPaymentLabel = new JLabel("₹0.00");
        totalPaymentLabel = new JLabel("₹0.00");
        interestRateLabel = new JLabel("10.75%");
        
        // Financial Info fields
        incomeField = new JTextField(10);
        
        String[] employmentOptions = {"Full-time", "Part-time", "Self-employed", "Unemployed", "Retired"};
        employmentStatusCombo = new JComboBox<>(employmentOptions);
        
        employerField = new JTextField(20);
        jobTitleField = new JTextField(20);
        employmentDurationField = new JTextField(10);
        creditScoreField = new JTextField(10);
        debtsField = new JTextField(10);
        
        // Buttons
        submitButton = new JButton("Submit Application");
        cancelButton = new JButton("Cancel");
    }
    
    /**
     * Layout the UI components.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Loan Details Panel
        JPanel loanPanel = new JPanel(new GridBagLayout());
        loanPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Loan Amount
        gbc.gridx = 0;
        gbc.gridy = 0;
        loanPanel.add(new JLabel("Loan Amount (₹):"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loanPanel.add(amountField, gbc);
        
        // Loan Purpose
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        loanPanel.add(new JLabel("Loan Purpose:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loanPanel.add(purposeField, gbc);
        
        // Loan Duration
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        loanPanel.add(new JLabel("Loan Duration:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loanPanel.add(durationSlider, gbc);
        
        // Interest Rate
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
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
        
        // Financial Info Panel
        JPanel financialPanel = new JPanel(new GridBagLayout());
        financialPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Annual Income
        gbc.gridx = 0;
        gbc.gridy = 0;
        financialPanel.add(new JLabel("Annual Income (₹):"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        financialPanel.add(incomeField, gbc);
        
        // Employment Status
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        financialPanel.add(new JLabel("Employment Status:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        financialPanel.add(employmentStatusCombo, gbc);
        
        // Employer
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        financialPanel.add(new JLabel("Employer:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        financialPanel.add(employerField, gbc);
        
        // Job Title
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        financialPanel.add(new JLabel("Job Title:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        financialPanel.add(jobTitleField, gbc);
        
        // Employment Duration
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        financialPanel.add(new JLabel("Employment Duration (months):"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        financialPanel.add(employmentDurationField, gbc);
        
        // Credit Score
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        financialPanel.add(new JLabel("Credit Score:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        financialPanel.add(creditScoreField, gbc);
        
        // Existing Debts
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        financialPanel.add(new JLabel("Existing Debts (₹):"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        financialPanel.add(debtsField, gbc);
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Loan Details", loanPanel);
        tabbedPane.addTab("Financial Information", financialPanel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        // Add everything to main panel
        add(tabbedPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Set up action listeners for the buttons and other components.
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
                submitApplication();
            }
        });
        
        // Duration slider
        durationSlider.addChangeListener(e -> updatePaymentCalculation());
        
        // Amount field
        amountField.addActionListener(e -> updatePaymentCalculation());
        amountField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                updatePaymentCalculation();
            }
        });
    }
    
    /**
     * Load existing financial information for the user.
     */
    private void loadFinancialInfo() {
        FinancialInfo financialInfo = financialController.getFinancialInfoByUserId(user.getUserId());
        
        if (financialInfo != null) {
            incomeField.setText(String.valueOf(financialInfo.getAnnualIncome()));
            
            String employmentStatus = financialInfo.getEmploymentStatus();
            for (int i = 0; i < employmentStatusCombo.getItemCount(); i++) {
                if (employmentStatusCombo.getItemAt(i).equalsIgnoreCase(employmentStatus)) {
                    employmentStatusCombo.setSelectedIndex(i);
                    break;
                }
            }
            
            employerField.setText(financialInfo.getEmployerName());
            jobTitleField.setText(financialInfo.getJobTitle());
            employmentDurationField.setText(String.valueOf(financialInfo.getEmploymentDuration()));
            
            if (financialInfo.getCreditScore() > 0) {
                creditScoreField.setText(String.valueOf(financialInfo.getCreditScore()));
            }
            
            if (financialInfo.getExistingDebts() > 0) {
                debtsField.setText(String.valueOf(financialInfo.getExistingDebts()));
            }
        }
    }
    
    /**
     * Update payment calculation based on entered amount and selected duration.
     */
    private void updatePaymentCalculation() {
        double amount = 0;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException ex) {
            // Invalid amount, leave as 0
        }
        
        int months = durationSlider.getValue();
        double interestRate = 10.75; // Updated interest rate
        
        if (amount > 0 && months > 0) {
            double monthlyRate = interestRate / 100.0 / 12.0;
            double monthlyPayment = amount * monthlyRate * Math.pow(1 + monthlyRate, months) 
                                    / (Math.pow(1 + monthlyRate, months) - 1);
            double totalPayment = monthlyPayment * months;
            
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            // Replace $ with ₹ in the formatted currency
            String monthlyPaymentStr = currencyFormat.format(monthlyPayment).replace("$", "₹");
            String totalPaymentStr = currencyFormat.format(totalPayment).replace("$", "₹");
            
            monthlyPaymentLabel.setText(monthlyPaymentStr);
            totalPaymentLabel.setText(totalPaymentStr);
        } else {
            monthlyPaymentLabel.setText("₹0.00");
            totalPaymentLabel.setText("₹0.00");
        }
        
        interestRateLabel.setText(String.format("%.2f%%", interestRate));
    }
    
    /**
     * Validate input fields and submit the loan application.
     */
    private void submitApplication() {
        // Validate loan details
        if (!validateLoanDetails()) {
            return;
        }
        
        // Validate financial info
        if (!validateFinancialInfo()) {
            return;
        }
        
        // Save financial info
        saveFinancialInfo();
        
        // Create loan application
        LoanApplication application = new LoanApplication();
        application.setUserId(user.getUserId());
        application.setLoanAmount(Double.parseDouble(amountField.getText()));
        application.setLoanPurpose(purposeField.getText().trim());
        application.setDurationMonths(durationSlider.getValue());
        application.setInterestRate(10.75); // Updated interest rate
        
        if (loanController.createLoanApplication(application)) {
            JOptionPane.showMessageDialog(this,
                    "Loan application submitted successfully!",
                    "Application Submitted", JOptionPane.INFORMATION_MESSAGE);
            applicationCreated = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to submit loan application. Please try again.",
                    "Submission Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate loan details input fields.
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateLoanDetails() {
        // Check loan amount
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid loan amount greater than zero",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid numeric loan amount",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check loan purpose
        if (purposeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter the purpose of the loan",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * Validate financial information input fields.
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateFinancialInfo() {
        // Check annual income
        try {
            double income = Double.parseDouble(incomeField.getText());
            if (income <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid annual income greater than zero",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid numeric annual income",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check employment duration (if provided)
        if (!employmentDurationField.getText().trim().isEmpty()) {
            try {
                int duration = Integer.parseInt(employmentDurationField.getText());
                if (duration < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Employment duration cannot be negative",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid numeric employment duration in months",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        // Check credit score (if provided)
        if (!creditScoreField.getText().trim().isEmpty()) {
            try {
                int score = Integer.parseInt(creditScoreField.getText());
                if (score < 300 || score > 850) {
                    JOptionPane.showMessageDialog(this,
                            "Credit score should be between 300 and 850",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid numeric credit score",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        // Check existing debts (if provided)
        if (!debtsField.getText().trim().isEmpty()) {
            try {
                double debts = Double.parseDouble(debtsField.getText());
                if (debts < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Existing debts cannot be negative",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid numeric value for existing debts",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Save the financial information to the database.
     */
    private void saveFinancialInfo() {
        FinancialInfo financialInfo = new FinancialInfo();
        financialInfo.setUserId(user.getUserId());
        financialInfo.setAnnualIncome(Double.parseDouble(incomeField.getText()));
        financialInfo.setEmploymentStatus((String) employmentStatusCombo.getSelectedItem());
        financialInfo.setEmployerName(employerField.getText().trim());
        financialInfo.setJobTitle(jobTitleField.getText().trim());
        
        if (!employmentDurationField.getText().trim().isEmpty()) {
            financialInfo.setEmploymentDuration(Integer.parseInt(employmentDurationField.getText()));
        }
        
        if (!creditScoreField.getText().trim().isEmpty()) {
            financialInfo.setCreditScore(Integer.parseInt(creditScoreField.getText()));
        }
        
        if (!debtsField.getText().trim().isEmpty()) {
            financialInfo.setExistingDebts(Double.parseDouble(debtsField.getText()));
        }
        
        financialController.saveFinancialInfo(financialInfo);
    }
    
    /**
     * Check if a loan application was created.
     * @return true if application was created, false otherwise
     */
    public boolean isApplicationCreated() {
        return applicationCreated;
    }
} 